package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.FriendAdapter;

public class FriendsFragment extends Fragment {
    private FriendAdapter mFriendAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView listView = rootView.findViewById(R.id.friends_list);
        this.mFriendAdapter = new FriendAdapter(inflater.getContext());
        listView.setAdapter(mFriendAdapter);
        SearchView view = rootView.findViewById(R.id.friends_filter);
        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FriendsFragment.this.mFriendAdapter.setFilter(newText);
                return true;
            }
        });

        return rootView;
    }

    public static Fragment newInstance(MainActivity mainActivity) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
