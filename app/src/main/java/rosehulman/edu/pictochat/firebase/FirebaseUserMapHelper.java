package rosehulman.edu.pictochat.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserMapHelper {
    public static void addDisplayNameListener(String email, final DisplayNameEventListener listener) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference()
                .child("user_map")
                .child(FirebaseKeyHelper.stringToKey(email));

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    listener.onError("No such user exists");
                    return;
                }
                listener.onResult(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }

    public interface DisplayNameEventListener {
        void onResult(String displayName);
        void onError(String message);
    }
}
