package cs121.ucsc.roomie.groupchannel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import cs121.ucsc.roomie.R;


public class GroupChannelActivity extends AppCompatActivity {
    String TAG = "GroupChannelActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_channel);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
       // setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24_dp);
        }
/*
        if (savedInstanceState == null) {
            // If started from launcher, load list of Open Channels
            Fragment fragment = GroupChannelListFragment.newInstance();

            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStack();

            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .commit();
        }
*/
        String channelUrl = getIntent().getStringExtra("houseURL");
        if(channelUrl != null) {
            // If started from notification
            Fragment fragment = GroupChatFragment.newInstance(channelUrl);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .replace(R.id.container_group_channel, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if(channelUrl == null){
            Log.d(TAG, "onCreate: group channel not found");
        }
    }

    interface onBackPressedListener {
        boolean onBack();
    }
    private onBackPressedListener mOnBackPressedListener;

    public void setOnBackPressedListener(onBackPressedListener listener) {
        mOnBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && mOnBackPressedListener.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
