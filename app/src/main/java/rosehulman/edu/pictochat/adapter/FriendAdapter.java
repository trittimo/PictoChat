package rosehulman.edu.pictochat.adapter;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.function.Predicate;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.firebase.FirebaseUserMapHelper;
import rosehulman.edu.pictochat.fragment.AddFriendDialogFragment;
import rosehulman.edu.pictochat.model.FriendModel;
import rosehulman.edu.pictochat.util.Constants;

public class FriendAdapter extends BaseAdapter implements ChildEventListener {
    private Context context;
    private ArrayList<FriendModel> friends = new ArrayList<>();
    private String filter;

    private DatabaseReference mDatabaseReference;

    public FriendAdapter(Context context, DatabaseReference databaseReference) {
        this.context = context;
        this.mDatabaseReference = databaseReference.child("friends");
    }

    public void remove(String key) {
        for (int i = 0; i < friends.size(); i++) {
            if (friends.get(i).getKey().equals(key)) {
                friends.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void add(final FriendModel model) {
        if (model.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            Toast.makeText(FriendAdapter.this.context, "You cannot add yourself", Toast.LENGTH_SHORT).show();
            return;
        }
        mDatabaseReference.push().setValue(model);
    }

    @Override
    public int getCount() {
        if (filter == null) {
            return friends.size();
        }
        ArrayList<FriendModel> copy = new ArrayList<>(friends);
        copy.removeIf(new Predicate<FriendModel>() {
            @Override
            public boolean test(FriendModel s) {
                return !s.getName().toLowerCase().contains(filter) && !s.getEmail().toLowerCase().contains(filter);
            }
        });
        return copy.size();
    }

    @Override
    public FriendModel getItem(int position) {
        if (filter == null) {
            return friends.get(position);
        }
        ArrayList<FriendModel> copy = new ArrayList<>(friends);
        copy.removeIf(new Predicate<FriendModel>() {
            @Override
            public boolean test(FriendModel s) {
                return !s.getName().toLowerCase().contains(filter) && !s.getEmail().toLowerCase().contains(filter);
            }
        });
        return copy.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.friends_list_item, parent, false);
        }

        FriendModel friend = getItem(position);
        TextView friendNameTextView = view.findViewById(R.id.friend_name_text);
        friendNameTextView.setText(friend.getName());

        final ImageButton addFriendButton = view.findViewById(R.id.options_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(FriendAdapter.this.context, addFriendButton);
                menu.getMenuInflater().inflate(R.menu.friend_option_menu, menu.getMenu());

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(FriendAdapter.this.context, "Removed friend", Toast.LENGTH_SHORT).show();
                        FriendModel friend = getItem(position);
                        mDatabaseReference.child(friend.getKey()).removeValue();
                        return true;
                    }
                });

                menu.show();
            }
        });
        return view;
    }

    public void setFilter(String filter) {
        if (filter.isEmpty()) {
            this.filter = null;
            notifyDataSetChanged();
            return;
        }
        this.filter = filter.toLowerCase();
        notifyDataSetChanged();
    }

    @Override
    public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
        final FriendModel friend = dataSnapshot.getValue(FriendModel.class);
        friend.setKey(dataSnapshot.getKey());
        FirebaseUserMapHelper.addDisplayNameListener(friend.getEmail(), new FirebaseUserMapHelper.DisplayNameEventListener() {
            @Override
            public void onResult(String displayName) {
                friend.setName(displayName);
                friends.add(friend);
                notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(FriendAdapter.this.context, message, Toast.LENGTH_SHORT).show();
                mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
            }
        });
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // This shouldn't be possible, so it's not implemented
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        remove(key);
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        // Not implemented
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Not implemented
    }

    public void clear() {
        friends.clear();
    }
}
