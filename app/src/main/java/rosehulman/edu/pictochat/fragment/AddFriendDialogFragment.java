package rosehulman.edu.pictochat.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.adapter.FriendAdapter;
import rosehulman.edu.pictochat.firebase.FirebaseUserMapHelper;
import rosehulman.edu.pictochat.model.FriendModel;

public class AddFriendDialogFragment extends DialogFragment {
    private FriendAdapter friendAdapter;


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    public void setFriendAdapter(FriendAdapter adapter) {
        this.friendAdapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_friend_dialog_title);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_friend_dialog, null);
        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText emailEditText = view.findViewById(R.id.email_address);
                String email = emailEditText.getText().toString();
                friendAdapter.add(new FriendModel(email));
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }
}
