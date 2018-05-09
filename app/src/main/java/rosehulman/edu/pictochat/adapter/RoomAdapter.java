package rosehulman.edu.pictochat.adapter;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.function.Predicate;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.ChatRoomActivity;
import rosehulman.edu.pictochat.model.RoomModel;

public class RoomAdapter extends BaseAdapter implements ChildEventListener{
    private Context context;
    private ArrayList<RoomModel> rooms = new ArrayList<>();
    private String filter;
    public static final String ROOM_ID_KEY = "room_id_key";
    private DatabaseReference mDatabaseReference;

    public RoomAdapter(Context context, DatabaseReference databaseReference) {
        this.context = context;
        this.mDatabaseReference = databaseReference;
    }

    public void remove(String id) {
        for (int i =0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(id)) {
                rooms.remove(i);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void add(RoomModel model) {
        DatabaseReference userRooms = mDatabaseReference
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("rooms");
        DatabaseReference allRooms = mDatabaseReference.child("rooms");
        allRooms.child(model.getId()).setValue(model);
        userRooms.child(model.getId()).setValue(model.getTitle());
    }

    @Override
    public int getCount() {
        if (filter == null) {
            return rooms.size();
        }
        ArrayList<RoomModel> copy = new ArrayList<>(rooms);
        copy.removeIf(new Predicate<RoomModel>() {
            @Override
            public boolean test(RoomModel s) {
                return !s.getTitle().toLowerCase().contains(filter);
            }
        });
        return copy.size();
    }

    @Override
    public RoomModel getItem(int position) {
        if (filter == null) {
            return rooms.get(position);
        }
        ArrayList<RoomModel> copy = new ArrayList<>(rooms);
        copy.removeIf(new Predicate<RoomModel>() {
            @Override
            public boolean test(RoomModel s) {
                return !s.getTitle().toLowerCase().contains(filter);
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
            view = LayoutInflater.from(context).inflate(R.layout.rooms_list_item, parent, false);
        }

        RoomModel room = getItem(position);
        final TextView roomNameTextView = view.findViewById(R.id.room_name_text);
        roomNameTextView.setText(room.getTitle());

        final ImageButton optionsMenuButton = view.findViewById(R.id.rooms_options_button);
        optionsMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(RoomAdapter.this.context, optionsMenuButton);
                menu.getMenuInflater().inflate(R.menu.room_option_menu, menu.getMenu());
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.remove_room:
                                Toast.makeText(RoomAdapter.this.context, "Removed room", Toast.LENGTH_SHORT).show();
                                RoomModel room = getItem(position);
                                mDatabaseReference
                                        .child("users")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .child("rooms")
                                        .child(room.getId())
                                        .removeValue();

                                return true;
                            case R.id.edit_room:
                                // TODO
                                return true;
                        }

                        return true;
                    }
                });

                menu.show();
            }
        });

        roomNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra(ROOM_ID_KEY, roomNameTextView.getText().toString());
                context.startActivity(intent);
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
        this.filter = filter;
        notifyDataSetChanged();
    }


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        RoomModel model = new RoomModel();
        model.setId(dataSnapshot.getKey());
        model.setTitle(dataSnapshot.getValue().toString());
        rooms.add(model);
        notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        // Not implemented
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        remove(dataSnapshot.getKey());
        notifyDataSetChanged();
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
        rooms.clear();
    }
}
