package cs121.ucsc.roomie.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import cs121.ucsc.roomie.MainActivity;
import cs121.ucsc.roomie.OpenChannelStore;
import cs121.ucsc.roomie.R;
import cs121.ucsc.roomie.User;
import cs121.ucsc.roomie.groupchannel.GroupChannelActivity;
import cs121.ucsc.roomie.openchannel.OpenChannelActivity;
import cs121.ucsc.roomie.utils.PreferenceUtils;

public class MessageActivity2 extends AppCompatActivity {
    final String TAG = "MessageActivity2";
    public ArrayList<User> houseUserList;
    private List<String> channelUserList;
    private List<Boolean> hasURL;
    private Toolbar mToolbar;
    private static boolean houseChatFlag;
    private static String HOUSE_URL;
    private static OpenChannel OPEN_HOUSE;
    private static String houseName;
    private static GroupChannel houseChannel;
    FirebaseAuth mAuth;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity2);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        Log.d("MessageActivity2", "passed setContentView");
        //       mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        Log.d("MessageActivity2", "passed findViewById");
        //      setSupportActionBar(mToolbar);
        // houseChannel = new GroupChannel();
        findViewById(R.id.linear_layout_group_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity2.this, GroupChannelActivity.class);
                intent.putExtra("houseURL", HOUSE_URL);
                startActivity(intent);
            }
        });

        findViewById(R.id.linear_layout_open_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity2.this, OpenChannelActivity.class);
                startActivity(intent);
            }
        });



        // Displays the SDK version in a TextView
        String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
                BaseApplication.VERSION, SendBird.getSDKVersion());
        ((TextView) findViewById(R.id.text_main_versions)).setText(sdkVersion);

        houseUserList = MainActivity.houseUserList;
        channelUserList = new LinkedList<>();
        hasURL = new LinkedList<>();
        for (int i = 0; i < houseUserList.size(); i++) {
            channelUserList.add(houseUserList.get(i).msgID);
            Log.i(TAG, "onCreate: " + channelUserList.get(i));
        }

        //  channelUserList.add(MainActivity.currUser.msgID);
        
            Log.d(TAG, "onCreate: house chat does not exist. Creating new chat");
            createHouseChat();
            MainActivity.groupChatExists = true;
        /*else {
            Log.i(TAG, "onCreate: chat  URL" + MainActivity.groupChatURL);
            GroupChannel.getChannel(MainActivity.groupChatURL, new GroupChannel.GroupChannelGetHandler() {
                @Override
                public void onResult(GroupChannel groupChannel, SendBirdException e) {
                    Log.i(TAG, "onResult: inside getChannel onResult()" + groupChannel.toString());
                    if (e != null) {
                        Log.i(TAG, "onResult: Inside getChannel onResult()");

                        return;
                    }
                    houseChannel = groupChannel;
                }
            });
            Log.i(TAG, "onCreate: " + houseChannel);
            for (int i = 0; i < channelUserList.size(); i++) {
                houseChannel.inviteWithUserId(houseUserList.get(i).msgID, new GroupChannel.GroupChannelInviteHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            Log.d(TAG, "onResult: error inviting peeps to house channel");
                        }
                    }
                });
            }
        }*/
        establishOpenChannel();
    }

    /**
     * Unregisters all push tokens for the current user so that they do not receive any notifications,
     * then disconnects from SendBird.
     */
    private void disconnect() {
        SendBird.unregisterPushTokenAllForCurrentUser(new SendBird.UnregisterPushTokenHandler() {
            @Override
            public void onUnregistered(SendBirdException e) {
                if (e != null) {
                    // Error!
                    e.printStackTrace();

                    // Don't return because we still need to disconnect.
                } else {
//                    Toast.makeText(MessageActivity2.this, "All push tokens unregistered.", Toast.LENGTH_SHORT).show();
                }

                SendBird.disconnect(new SendBird.DisconnectHandler() {
                    @Override
                    public void onDisconnected() {
                        PreferenceUtils.setConnected(MessageActivity2.this, false);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main:
                Intent intent = new Intent(MessageActivity2.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private GroupChannelListQuery getHouseChannel(List<String> list) {
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setUserIdsIncludeFilter(list, GroupChannelListQuery.QueryType.AND);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {
                    Log.d("MessageActivity2", "error retrieving house channel");
                    return;
                }
            }
        });
        return channelListQuery;
    }

    private void createHouseChat() {
        GroupChannel.createChannelWithUserIds(channelUserList, true, MainActivity.currUser.houseName,
                null, null, null, new GroupChannel.GroupChannelCreateHandler() {
                    @Override
                    public void onResult(GroupChannel groupChannel, SendBirdException e) {
                        if (e != null) {
                            //error
                            Log.e(TAG, "onResult: error creating channel", e);
                            return;
                        } else {
                            Log.d(TAG, "onResult: group chat created successfully");
                            HOUSE_URL = groupChannel.getUrl();
                            Log.i(TAG, "onResult: chhannel URL=" + groupChannel.getUrl());
                            houseChannel = groupChannel;
                            for (int i = 0; i < MainActivity.houseUserList.size(); i++) {
                                cs121.ucsc.roomie.User holder = houseUserList.get(i);
                                holder.msgURL = HOUSE_URL;
                                database.child("UserData").child(holder.userEmail.substring(0,
                                        holder.userEmail.indexOf('@'))).setValue(holder);
                            }

                        }
                    }
                });
    }

    private void addToExistingChat() {
        GroupChannel.getChannel(MainActivity.currUser.msgURL, new GroupChannel.GroupChannelGetHandler() {
            @Override
            public void onResult(GroupChannel groupChannel, SendBirdException e) {
                if (e != null) {
                    //error
                    return;
                }
                houseChannel = groupChannel;
            }
        });
        for (int i = 0; i < channelUserList.size(); i++) {
            houseChannel.inviteWithUserId(houseUserList.get(i).msgID, new GroupChannel.GroupChannelInviteHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        Log.d(TAG, "onResult: error inviting peeps to house channel");
                    }
                }
            });
        }
    }

    private void establishOpenChannel(){

        database.child("OpenHouse").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean channelExists = false;
                final OpenChannel newOpenChannel;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OpenChannelStore openChannelStore = snapshot.getValue(OpenChannelStore.class);
                    if (openChannelStore.channelExists){
                        channelExists = true;
                    }
                }
                if (channelExists == false){
                    OpenChannel.createChannel(MainActivity.currUser.houseName, null, null,
                            new OpenChannel.OpenChannelCreateHandler() {
                                @Override
                                public void onResult(OpenChannel openChannel, SendBirdException e) {
                                    if (e != null){
                                        return;
                                    }else{
                                        MessageActivity2.OPEN_HOUSE = openChannel;
                                    }
                                }
                            });
                    OpenChannelStore newChannel = new OpenChannelStore(MainActivity.currUser.houseName);
                    newChannel.channelExists = true;
                    database.child("OpenHouse").child(MainActivity.currUser.houseName).setValue(newChannel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // database.child("OpenHouse").child().setValue(OpenChannelStore);

    }
}
