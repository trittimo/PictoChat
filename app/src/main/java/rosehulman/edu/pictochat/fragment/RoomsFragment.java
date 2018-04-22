package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.FriendAdapter;

public class RoomsFragment extends Fragment {
    public RoomsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rooms, container, false);
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
