package rosehulman.edu.pictochat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.model.FriendListItemModel;

public class FriendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<FriendListItemModel> friends = new ArrayList<>();

    public FriendAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 50; i++) {
            friends.add(new FriendListItemModel());
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.friends_list_item, parent, false);
        }

        FriendListItemModel friend = friends.get(position);
        TextView friendNameTextView = view.findViewById(R.id.friend_name_text);
        friendNameTextView.setText(friend.getName());
        return view;
    }
}
