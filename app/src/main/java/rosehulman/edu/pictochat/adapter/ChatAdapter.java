package rosehulman.edu.pictochat.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.firebase.FirebaseUserMapHelper;
import rosehulman.edu.pictochat.model.MessageModel;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> implements ChildEventListener {

    private ArrayList<MessageModel> mMessages;
    private LinearLayoutManager mLayoutManager;
    private String mRoomId;

    public ChatAdapter(String roomId, LinearLayoutManager mLayoutManager) {
        this.mRoomId = roomId;
        this.mMessages = new ArrayList<>();
        this.mLayoutManager = mLayoutManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageBitmap(mMessages.get(holder.getAdapterPosition()).getBitmap());
        String fromEmail = mMessages.get(holder.getAdapterPosition()).getFrom();
        holder.fromTextView.setText(fromEmail);
        holder.contentTextView.setText(mMessages.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        final MessageModel message = dataSnapshot.getValue(MessageModel.class);
        message.setKey(dataSnapshot.getKey());
        mMessages.add(message);
        notifyDataSetChanged();
        mLayoutManager.scrollToPosition(getItemCount() - 1);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public void clear() {
        mMessages.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView fromTextView;
        public TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.chat_image_view);
            this.fromTextView = itemView.findViewById(R.id.message_from);
            this.contentTextView = itemView.findViewById(R.id.message_content);
        }
    }

    public void add(Bitmap message, String content) {
        MessageModel model = new MessageModel();
        model.setBase64Bitmap(message);
        model.setContent(content);
        model.setFrom(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        FirebaseDatabase.getInstance().getReference()
                .child("rooms")
                .child(mRoomId)
                .child("messages")
                .push()
                .setValue(model);

    }
}
