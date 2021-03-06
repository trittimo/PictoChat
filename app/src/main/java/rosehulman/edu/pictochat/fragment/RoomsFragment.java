package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.RoomAdapter;

public class RoomsFragment extends Fragment {
    private static final String DIALOG_ADD_ROOM = "add_room";

    private RoomAdapter mRoomsAdapter;
    private DatabaseReference mDatabaseReference;

    public RoomsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        ListView listView = rootView.findViewById(R.id.rooms_list);

        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        this.mRoomsAdapter = new RoomAdapter(inflater.getContext(), mDatabaseReference);
        listView.setAdapter(mRoomsAdapter);

        SearchView view = rootView.findViewById(R.id.rooms_filter);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RoomsFragment.this.mRoomsAdapter.setFilter(newText);
                return true;
            }
        });

        FloatingActionButton addRoomButton = rootView.findViewById(R.id.add_room_button);
        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRoomDialogFragment fragment = new AddRoomDialogFragment();
                fragment.setRoomAdapter(mRoomsAdapter);
                fragment.show(getActivity().getFragmentManager(), DIALOG_ADD_ROOM);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRoomsAdapter.clear();
        mDatabaseReference
                .child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("rooms")
                .addChildEventListener(mRoomsAdapter);
        mRoomsAdapter.checkLastMessage();
    }

    public static Fragment newInstance(MainActivity mainActivity) {
        RoomsFragment fragment = new RoomsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
