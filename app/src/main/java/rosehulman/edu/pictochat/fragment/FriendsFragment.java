package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.FriendAdapter;

public class FriendsFragment extends Fragment {
    private static final String DIALOG_ADD_FRIEND = "add_friend";
    private static final String DIALOG_ADD_ROOM = "add_room";
    private FriendAdapter mFriendAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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

        Button addFriendButton = rootView.findViewById(R.id.add_friend_button);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendDialogFragment fragment = new AddFriendDialogFragment();
                fragment.setFriendAdapter(mFriendAdapter);
                fragment.show(getActivity().getFragmentManager(), DIALOG_ADD_FRIEND);
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
