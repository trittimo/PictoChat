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

import java.util.ArrayList;
import java.util.function.Predicate;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.ChatRoomActivity;
import rosehulman.edu.pictochat.model.FriendModel;
import rosehulman.edu.pictochat.model.RoomModel;

public class RoomAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<RoomModel> rooms = new ArrayList<>();
    private String filter;
    public static final String ROOM_ID_KEY = "room_id_key";

    public RoomAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 5; i++) {
            rooms.add(new RoomModel());
        }
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
                return !s.getName().toLowerCase().contains(filter);
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
                return !s.getName().toLowerCase().contains(filter);
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
        roomNameTextView.setText(room.getName());

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
                                rooms.remove(getItem(position));
                                RoomAdapter.this.notifyDataSetChanged();
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
}
