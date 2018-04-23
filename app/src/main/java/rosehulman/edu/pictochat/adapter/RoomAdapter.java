package rosehulman.edu.pictochat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.ChatRoomActivity;
import rosehulman.edu.pictochat.model.RoomModel;

/**
 * Created by dongmj on 4/22/2018.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    public static final String ROOM_ID_KEY = "ROOM_ID_KEY";

    ArrayList<RoomModel> mRooms;
    Context mContext;

    public RoomAdapter(Context context){
        mContext = context;
        mRooms = new ArrayList<RoomModel>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mName.setText(mRooms.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    public void addNewRoom(){
        mRooms.add(new RoomModel("Temporary Room " + mRooms.size()));
        notifyDataSetChanged();
    }

    public void openRoom(RoomModel room){
        Intent intent = new Intent(mContext, ChatRoomActivity.class);
        intent.putExtra(ROOM_ID_KEY, room.getName());
        mContext.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.room_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openRoom(mRooms.get(getAdapterPosition()));
                }
            });
        }
    }

}
