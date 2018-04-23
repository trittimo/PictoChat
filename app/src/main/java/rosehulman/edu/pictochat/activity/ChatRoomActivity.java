package rosehulman.edu.pictochat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.adapter.RoomAdapter;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        String roomName = intent.getStringExtra(RoomAdapter.ROOM_ID_KEY);
        setTitle(roomName);
    }
}
