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

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.model.FriendListItemModel;

public class FriendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendListItemModel> friends = new ArrayList<>();

    public FriendAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 5; i++) {
            friends.add(new FriendListItemModel("John Doe " + i, "default@gmail.com"));
        }
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
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

        FriendListItemModel friend = friends.get(position);
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
                        friends.remove(position);
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
}
