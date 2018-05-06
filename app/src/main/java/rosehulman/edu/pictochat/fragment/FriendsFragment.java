package rosehulman.edu.pictochat.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.activity.MainActivity;
import rosehulman.edu.pictochat.adapter.FriendAdapter;
import rosehulman.edu.pictochat.model.FriendModel;
import rosehulman.edu.pictochat.util.Constants;

public class FriendsFragment extends Fragment {
    private static final String DIALOG_ADD_FRIEND = "add_friend";
    private FriendAdapter mFriendAdapter;
    private String mAuthId;
    private DatabaseReference mDatabaseReference;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        ListView listView = rootView.findViewById(R.id.friends_list);

        this.mAuthId = getActivity().getIntent().getStringExtra(Constants.EXTRA_USER_ID);
        this.mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(this.mAuthId);

        this.mFriendAdapter = new FriendAdapter(inflater.getContext(), mDatabaseReference.child("friends"));
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

        FloatingActionButton addFriendButton = rootView.findViewById(R.id.add_friend_button);
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

    @Override
    public void onResume() {
        super.onResume();
        mFriendAdapter.clear();
        mDatabaseReference.child("friends").addChildEventListener(mFriendAdapter);

    }

    public static Fragment newInstance(MainActivity mainActivity) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}
