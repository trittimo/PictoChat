package rosehulman.edu.pictochat.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import rosehulman.edu.pictochat.R;
import rosehulman.edu.pictochat.adapter.ChatAdapter;
import rosehulman.edu.pictochat.adapter.RoomAdapter;
import rosehulman.edu.pictochat.firebase.FirebaseKeyHelper;
import rosehulman.edu.pictochat.model.FriendModel;
import rosehulman.edu.pictochat.model.MessageModel;
import rosehulman.edu.pictochat.view.CanvasView;

public class ChatRoomActivity extends AppCompatActivity {

    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private String roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Intent intent = getIntent();
        String roomName = intent.getStringExtra(RoomAdapter.ROOM_NAME_KEY);
        this.roomId = intent.getStringExtra(RoomAdapter.ROOM_ID_KEY);
        setTitle(roomName);

        RecyclerView chatRecyclerView = findViewById(R.id.chat_recycler_view);

        this.mLayoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(mLayoutManager);

        this.mChatAdapter = new ChatAdapter(roomId, mLayoutManager);
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

                        mChatAdapter.add(bitmap);
                    }
                });

                builder.create().show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChatAdapter.clear();
        FirebaseDatabase.getInstance().getReference()
                .child("rooms")
                .child(roomId)
                .child("messages")
                .addChildEventListener(mChatAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_room, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder;
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(ChatRoomActivity.this, SettingsActivity.class));
                return true;
            case R.id.action_info:
                builder = new AlertDialog.Builder(this);
                builder.setTitle("Room Information");
                builder.setMessage("Room ID: " + roomId);
                builder.show();
                return true;
            case R.id.action_add_user:
                builder = new AlertDialog.Builder(this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
                builder.setView(view);

                ListView listView = view.findViewById(R.id.friends_list_view);
                ArrayList<String> friends = new ArrayList<>();

                ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, friends);
                listView.setAdapter(adapter);

                addFriends(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        FirebaseDatabase.getInstance().getReference()
                                .child("rooms")
                                .child(roomId)
                                .child("users")
                                .child(FirebaseKeyHelper.stringToKey(item))
                                .setValue(true);
                    }
                });

                builder.create().show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addFriends(final ArrayAdapter<String> adapter) {
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("friends")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot friendValue : dataSnapshot.getChildren()) {
                            FriendModel friend = friendValue.getValue(FriendModel.class);
                            adapter.add(friend.getEmail());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
