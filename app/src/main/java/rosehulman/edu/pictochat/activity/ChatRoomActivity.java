package rosehulman.edu.pictochat.activity;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.adapter.ChatAdapter;
import rosehulman.edu.pictochat.adapter.RoomAdapter;
import rosehulman.edu.pictochat.view.CanvasView;

public class ChatRoomActivity extends AppCompatActivity {

    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        String roomName = intent.getStringExtra(RoomAdapter.ROOM_ID_KEY);
        setTitle(roomName);

        RecyclerView chatRecyclerView = findViewById(R.id.chat_recycler_view);

        this.mLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(mLayoutManager);

        this.mChatAdapter = new ChatAdapter();
        chatRecyclerView.setAdapter(mChatAdapter);

        Button drawButton = findViewById(R.id.draw_button);
        drawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoomActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_draw, null);
                builder.setView(view);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CanvasView canvas = view.findViewById(R.id.canvas_view);
                        Bitmap bitmap = canvas.getBitmap();
                        mChatAdapter.addMessage(bitmap);
                        mLayoutManager.scrollToPosition(mChatAdapter.getItemCount() - 1);
                    }
                });

                builder.create().show();
            }
        });
    }
}
