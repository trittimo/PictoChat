package rosehulman.edu.pictochat.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import rosehulman.edu.pictochat.model.RoomModel;

public class FirebaseRoomHelper {
    private Context mContext;
    private DatabaseReference mDatabase;
    public FirebaseRoomHelper(Context context) {
        this.mContext = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("rooms");
    }

    public void createRoom(final String roomTitle, final String roomId) {
        final DatabaseReference room = mDatabase.child(roomId);

        room.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean exists = false;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("users")) {
                            for (DataSnapshot user : child.getChildren()) {
                                String email = FirebaseKeyHelper.stringToKey(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                if (user.getKey().equals(email)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (!exists) {
                                Toast.makeText(mContext, "You cannot join that room", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                }

                room.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("rooms")
                                    .child(roomId)
                                    .child("title")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("users")
                                                    .child(FirebaseAuth.getInstance().getUid())
                                                    .child("rooms")
                                                    .child(roomId)
                                                    .setValue(dataSnapshot.getValue());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            room.child("title").setValue(roomTitle);
                            room.child("users").child(FirebaseKeyHelper.stringToKey(email)).setValue(true);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("users")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("rooms")
                                    .child(roomId)
                                    .setValue(roomTitle);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
