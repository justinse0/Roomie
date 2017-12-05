package cs121.ucsc.roomie.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cs121.ucsc.roomie.MainActivity;
import cs121.ucsc.roomie.R;
import cs121.ucsc.roomie.User;
import cs121.ucsc.roomie.groupchannel.GroupChannelActivity;
import cs121.ucsc.roomie.openchannel.OpenChannelActivity;
import cs121.ucsc.roomie.utils.PreferenceUtils;

public class MessageActivity2 extends AppCompatActivity {

    public ArrayList<User> houseUserList;
    private List<String> channelUserList;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity2);
Log.d("MessageActivity2", "passed setContentView");
 //       mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        Log.d("MessageActivity2", "passed findViewById");
  //      setSupportActionBar(mToolbar);

        findViewById(R.id.linear_layout_group_channels).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MessageActivity2.this, GroupChannelActivity.class);
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

        findViewById(R.id.button_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Unregister push tokens and disconnect
                disconnect();
            }
        });

        // Displays the SDK version in a TextView
        String sdkVersion = String.format(getResources().getString(R.string.all_app_version),
                BaseApplication.VERSION, SendBird.getSDKVersion());
        ((TextView) findViewById(R.id.text_main_versions)).setText(sdkVersion);

        houseUserList = MainActivity.houseUserList;
        channelUserList = new LinkedList<>();
        for(int i=0; i < houseUserList.size(); i++){
            channelUserList.add(houseUserList.get(i).name);
        }
        GroupChannelListQuery houseChannel = getHouseChannel(channelUserList);
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
        switch (item.getItemId()){
            case R.id.menu_main:
                Intent intent = new Intent(MessageActivity2.this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    private GroupChannelListQuery getHouseChannel(List<String> list){
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setUserIdsIncludeFilter(list,  GroupChannelListQuery.QueryType.AND);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e!= null){
                    Log.d("MessageActivity2", "error retrieving house channel");
                    return;
                }
            }
        });
        return channelListQuery;
    }
}
