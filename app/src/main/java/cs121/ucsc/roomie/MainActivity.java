package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;

import java.util.ArrayList;

import cs121.ucsc.roomie.main.MessageActivity2;
import cs121.ucsc.roomie.utils.PreferenceUtils;
import cs121.ucsc.roomie.utils.PushUtils;

import static cs121.ucsc.roomie.NewUserActivity.HouseNameStore;
import static cs121.ucsc.roomie.NewUserActivity.NameStore;
import static cs121.ucsc.roomie.NewUserActivity.PasswordStore;

public class MainActivity extends Activity {
    private final String APPID = new String("A21A2BDC-6192-4A27-A4A9-F3FEA23F2CFA");
    public static final String VERSION = "3.0.38";
    private ArrayList<String> roomies;
    private ListView listView;
    public static ArrayList<User> houseUserList;
    public static User currUser;
    private String userPass;
    private boolean secondPress;
    private String[] userArray;
    Button displayMates ;
    Button goTodo;
    Button messageStart;
    Button splitBill;
    Button viewprof;
    public static String[] staticNames;
    FirebaseAuth mAuth;
    DatabaseReference database;
    private int counter;
    private ContentLoadingProgressBar mProgressBar;

    public static User getUser(){
        return currUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // roomies  = new ArrayList<String>();
        counter=0;
        listView = new ListView(this);
        houseUserList = new ArrayList<User>();
        userPass = (getIntent().getStringExtra("UserPass"));

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();



        //initialize the connection to SendBird servers
        SendBird.init(APPID, this);

        //create the list and buttons for displaying roommates
        //and find the current user
        listView = (ListView) findViewById(R.id.roomieList);
        displayMates = (Button) findViewById(R.id.roomies);
        goTodo = (Button) findViewById(R.id.todo);
        splitBill = (Button) findViewById(R.id.billSplit);
        viewprof = (Button) findViewById(R.id.profile);
        messageStart = (Button) findViewById(R.id.messaging);
        secondPress = false;

        // A loading indicator
        mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);


        database.child("UserData").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user = snapshot.getValue(User.class);
                            String x = user.password;
                            if(x.equals(userPass)){
                              currUser = user;
                            }
                        }
                        for( DataSnapshot snapshot : dataSnapshot.getChildren()){
                            User user = snapshot.getValue(User.class);

                            if(!(user.name.equals(currUser.name)) && user.houseName.equals(currUser.houseName)){
                                System.out.println(user.name);
                                houseUserList.add(user);
                                counter+=1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

      //  for (int i=0; i<counter; i++){
      //      roomies.add(houseUserList.get(i).name);
      //  }
     /*   ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, roomies);
        if(listArrayAdapter != null) {
           listView.setAdapter(listArrayAdapter);
        }
*/


        splitBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent((cs121.ucsc.roomie.MainActivity.this), BillSplitActivity.class);
                startActivity(intent);
            }
        });
        goTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(cs121.ucsc.roomie.MainActivity.this, ToDoActivity.class);
               // intent.putExtra("CurrentUser", currUser.userPass);
                startActivity(intent);
            }
        });
        viewprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cs121.ucsc.roomie.MainActivity.this, ProfileActivity.class);

                startActivity(intent);
            }
        });
        messageStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendBirdConnect();
                // Intent intent = new Intent(MainActivity.this, MessageActivity2.class);
                //  intent.putExtra("CurrentUser", userPass);
                // startActivity(intent);
            }
        });
    }

    //DO NOT DISTURB BUTTON
    public void Busy(View v){
        //User.busy=1;
        //cs121.ucsc.roomie.User user = new User(User.name,User.houseAddress,User.houseName,User.msgURL,User.busy,User.password,User.userEmail);
        //database.child("UserData").child(NewUserActivity.ChoppedUser).setValue(user);
    }

    //public void StartMessaging(View view){
      //      Intent intent = new Intent(MainActivity.this, MessageActivity.class);
        //    intent.putExtra("CurrentUser", userPass);
          //  startActivity(intent);
    //}

    //public void ShowRoomies(View view){
      //if(listView.getVisibility() == View.INVISIBLE){
        //listView.setVisibility(View.VISIBLE);
        //}else{
          //  listView.setVisibility(View.INVISIBLE);
        //}
        //if(secondPress == false){
           // view.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
            //view.invalidate();
            //secondPress = true;
        //}else if(secondPress == true){
            //view.getBackground().clearColorFilter();
            //view.invalidate();
            //secondPress = false;
        //}
    //}

    /*******************************************************************************************
     * SendBird  imported functions
     */


    public void SendBirdConnect(){
        String userId = currUser.name;
        //replace  spaces in username
        userId = userId.replaceAll("\\s", "");
        String userNickname = userId;

        PreferenceUtils.setUserId(cs121.ucsc.roomie.MainActivity.this, userId);
        PreferenceUtils.setNickname(cs121.ucsc.roomie.MainActivity.this, userNickname);

        connectToSendBird(userId, userNickname);
    }
    /**
     * Attempts to connect a user to SendBird.
     * @param userId    The unique ID of the user.
     * @param userNickname  The user's nickname, which will be displayed in chats.
     */
    private void connectToSendBird(final String userId, final String userNickname) {
        // Show the loading indicator
        showProgressBar(true);

        SendBird.connect(userId, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(com.sendbird.android.User user, SendBirdException e) {
                // Callback received; hide the progress bar.
                showProgressBar(false);

                if (e != null) {
                    // Error!
                    Toast.makeText(
                            cs121.ucsc.roomie.MainActivity.this, "" + e.getCode() + ": " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show login failure snackbar
                    Toast.makeText(MainActivity.this, "Login to SendBird failed", Toast.LENGTH_LONG);

                    PreferenceUtils.setConnected(cs121.ucsc.roomie.MainActivity.this, false);
                    return;
                }

                PreferenceUtils.setNickname(cs121.ucsc.roomie.MainActivity.this, user.getNickname());
                PreferenceUtils.setProfileUrl(cs121.ucsc.roomie.MainActivity.this, user.getProfileUrl());
                PreferenceUtils.setConnected(cs121.ucsc.roomie.MainActivity.this, true);

                // Update the user's nickname
                updateCurrentUserInfo(userNickname);
                updateCurrentUserPushToken();

                // Proceed to MessageActivity2
                Intent intent = new Intent(cs121.ucsc.roomie.MainActivity.this, MessageActivity2.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    // Shows or hides the ProgressBar
    private void showProgressBar(boolean show) {
        /*if (show) {
            mProgressBar.show();
        } else {
            mProgressBar.hide();
        }*/
    }



    /**
     * Updates the user's nickname.
     * @param userNickname  The new nickname of the user.
     */
    private void updateCurrentUserInfo(final String userNickname) {
        SendBird.updateCurrentUserInfo(userNickname, null, new SendBird.UserInfoUpdateHandler() {
            @Override
            public void onUpdated(SendBirdException e) {
                if (e != null) {
                    // Error!
                    Toast.makeText(
                            cs121.ucsc.roomie.MainActivity.this, "" + e.getCode() + ":" + e.getMessage(),
                            Toast.LENGTH_SHORT)
                            .show();

                    // Show update failed snackbar
                    Toast.makeText(MainActivity.this, "Update user nickname failed", Toast.LENGTH_LONG);

                    return;
                }

                PreferenceUtils.setNickname(cs121.ucsc.roomie.MainActivity.this, userNickname);
            }
        });
    }

    /**
     * Update the user's push token.
     */
    private void updateCurrentUserPushToken() {
        PushUtils.registerPushTokenForCurrentUser(cs121.ucsc.roomie.MainActivity.this, null);
    }
/**
 *end Sendbird Functions
 *****************************************************************************************/






}
