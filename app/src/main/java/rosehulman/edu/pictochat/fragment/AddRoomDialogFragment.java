package rosehulman.edu.pictochat.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.adapter.RoomAdapter;
import rosehulman.edu.pictochat.firebase.FirebaseRoomHelper;
import rosehulman.edu.pictochat.model.RoomModel;

public class AddRoomDialogFragment extends DialogFragment {
    private RoomAdapter roomAdapter;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
    }

    public void setRoomAdapter(RoomAdapter adapter) {
        this.roomAdapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_room_dialog_title);

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_room_dialog, null);
        builder.setView(view);



        final FirebaseRoomHelper roomHelper = new FirebaseRoomHelper(getActivity());

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextView roomTitleTextView = view.findViewById(R.id.room_title);
                TextView roomIdTextView = view.findViewById(R.id.room_id);

                roomHelper.createRoom(roomTitleTextView.getText().toString(), roomIdTextView.getText().toString());
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Nothing to do here
            }
        });
        return builder.create();
    }
}
