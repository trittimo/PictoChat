package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.FriendAdapter;
import rosehulman.edu.pictochat.adapter.RoomAdapter;

public class RoomsFragment extends Fragment {
    private RoomAdapter mRoomsAdapter;

    public RoomsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
        ListView listView = rootView.findViewById(R.id.rooms_list);
        this.mRoomsAdapter = new RoomAdapter(inflater.getContext());
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
