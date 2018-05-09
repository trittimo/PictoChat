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
    private AddRoomCallback mCallback;
    public FirebaseRoomHelper(Context context, AddRoomCallback callback) {
        this.mContext = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("rooms");
        this.mCallback = callback;
    }

    public void createRoom(final String roomTitle, final String roomId) {
        final DatabaseReference room = mDatabase.child(roomId);

        room.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(mContext, "That room already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

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

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mContext, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface AddRoomCallback {
        void onRoomAdded(RoomModel room);
    }
}
