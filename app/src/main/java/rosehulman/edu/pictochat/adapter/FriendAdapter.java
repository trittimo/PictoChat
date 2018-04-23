package rosehulman.edu.pictochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.function.Predicate;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.model.FriendListItemModel;

public class FriendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendListItemModel> friends = new ArrayList<>();
    private String filter;

    public FriendAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 5; i++) {
            friends.add(new FriendListItemModel("John Doe " + i, "default@gmail.com"));
        }
    }

    @Override
    public int getCount() {
        if (filter == null) {
            return friends.size();
        }
        ArrayList<FriendListItemModel> copy = new ArrayList<>(friends);
        copy.removeIf(new Predicate<FriendListItemModel>() {
            @Override
            public boolean test(FriendListItemModel s) {
                return !s.getName().toLowerCase().contains(filter) && !s.getEmail().toLowerCase().contains(filter);
            }
        });
        return copy.size();
    }

    @Override
    public FriendListItemModel getItem(int position) {
        if (filter == null) {
            return friends.get(position);
        }
        ArrayList<FriendListItemModel> copy = new ArrayList<>(friends);
        copy.removeIf(new Predicate<FriendListItemModel>() {
            @Override
            public boolean test(FriendListItemModel s) {
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

        FriendListItemModel friend = getItem(position);
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
                        friends.remove(getItem(position));
                        FriendAdapter.this.notifyDataSetChanged();

                        // TODO: Actually remove the friend
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
            return;
        }
        this.filter = filter.toLowerCase();
        notifyDataSetChanged();
    }
}
