package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.RoomAdapter;

public class RoomsFragment extends Fragment {

    public RoomAdapter mAdapter;

    public RoomsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.rooms_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        textView.setText("This is the rooms fragment view");
        Button newRoomButton = (Button) rootView.findViewById(R.id.new_room_button);
        mAdapter = new RoomAdapter(getContext());
        newRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.addNewRoom();
            }
        });
        RecyclerView recycleView = (RecyclerView)rootView.findViewById(R.id.room_recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(mAdapter);
        return rootView;
    }


    public static Fragment newInstance(MainActivity mainActivity) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
