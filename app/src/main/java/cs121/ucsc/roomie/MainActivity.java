package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.design.widget.Snackbar;
//import android.support.v4.widget.ContentLoadingProgressBar;
//import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
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
import java.util.LinkedList;
import java.util.List;

import cs121.ucsc.roomie.main.MessageActivity2;
import cs121.ucsc.roomie.utils.PreferenceUtils;
import cs121.ucsc.roomie.utils.PushUtils;

import static cs121.ucsc.roomie.NewUserActivity.HouseNameStore;
import static cs121.ucsc.roomie.NewUserActivity.NameStore;
import static cs121.ucsc.roomie.NewUserActivity.PasswordStore;

public class MainActivity extends Activity {
    private final String APPID = new String("A21A2BDC-6192-4A27-A4A9-F3FEA23F2CFA");
    public static final String VERSION = "3.0.38";
    private  static List<String> roomies;
    private ListView listView;
    public static ArrayList<User> houseUserList;
    public static User currUser;
    private String userPass;
    private boolean secondPress;
    private String[] userArray;
    private boolean houseEstablished;

    Button displayMates ;
    ImageButton goTodo;
    ImageButton messageStart;
    ImageButton goPayment;
    ImageButton viewprof;
    Button disturb;
    Switch dndSwitch;

    public static String[] staticNames;
    FirebaseAuth mAuth;
    DatabaseReference database;
    private int counter;
    static int indexChop;
    static String userChop;
    static int alternate = 1;
    final String TAG = "Mainactivity.java";
    String userEmail;
    public static boolean openChatExists;
    public static boolean groupChatExists;
    private boolean currUserHasURL;
    public static String groupChatURL;
    private boolean userFound;
    public static boolean doNotDisturb;
    //private ContentLoadingProgressBar mProgressBar;

    public static User getUser(){
        return currUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // roomies  = new ArrayList<String>();
        Toast.makeText(cs121.ucsc.roomie.MainActivity.this, "Welcome!",
                Toast.LENGTH_SHORT).show();
        counter = 0;
        listView = new ListView(this);
        houseUserList = new ArrayList<User>();
        roomies = new LinkedList<>();
        userPass = (getIntent().getStringExtra("UserPass"));
        userEmail  = (getIntent().getStringExtra("UserEmail"));
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        groupChatExists = false;
        openChatExists = false;
        currUserHasURL = false;
        userFound = false;

        //initialize the connection to SendBird servers
        SendBird.init(APPID, this);

        //create the list and buttons for displaying roommates
        //and find the current user
        //listView = (ListView) findViewById(R.id.roomieList);
        goTodo = (ImageButton) findViewById(R.id.todo);
        goPayment = (ImageButton) findViewById(R.id.payment);
        viewprof = (ImageButton) findViewById(R.id.profile);
        messageStart = (ImageButton) findViewById(R.id.messaging);
        dndSwitch = (Switch) findViewById(R.id.dnd_switch);
        secondPress = false;

        // A loading indicator
        // mProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progress_bar_login);


       listupdate();

        //  for (int i=0; i<counter; i++){
        //      roomies.add(houseUserList.get(i).name);
        //  }
     /*   ArrayAdapter<String> listArrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, roomies);
        if(listArrayAdapter != null) {
           listView.setAdapter(listArrayAdapter);
        }
*/


        goPayment.setOnClickListener(new View.OnClickListener() {
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
            }
            //if(userFlag == true) {

            // }
            // Intent intent = new Intent(MainActivity.this, MessageActivity2.class);
            //  intent.putExtra("CurrentUser", userPass);
            // startActivity(intent);

        });
        dndSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNotDisturb = dndSwitch.isChecked();
                if (doNotDisturb) {
                    cs121.ucsc.roomie.User user = new User(MainActivity.currUser.name,
                            MainActivity.currUser.houseName,MainActivity.currUser.password,
                            MainActivity.currUser.houseAddress,1,MainActivity.currUser.userEmail,
                            MainActivity.currUser.msgURL, MainActivity.currUser.msgID);
                    database.child("UserData").child(userChop).setValue(user);
                    Toast.makeText(cs121.ucsc.roomie.MainActivity.this, "Do Not Disturb Mode On",
                            Toast.LENGTH_SHORT).show();
                } else if (!doNotDisturb) {
                    cs121.ucsc.roomie.User user = new User(MainActivity.currUser.name,
                            MainActivity.currUser.houseName,MainActivity.currUser.password,
                            MainActivity.currUser.houseAddress,0,MainActivity.currUser.userEmail,
                            MainActivity.currUser.msgURL,  MainActivity.currUser.msgID);
                    database.child("UserData").child(userChop).setValue(user);
                    Toast.makeText(cs121.ucsc.roomie.MainActivity.this, "Do Not Disturb Mode Off",
                            Toast.LENGTH_SHORT).show();
                }
                listupdate();
            }
        });
    }
    //update the lists as well as the database
    public void listupdate(){
        houseUserList.clear();
        roomies.clear();
        database.child("UserData").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    String x = user.userEmail;
                    if (x.equals(userEmail)) {
                        //
                        currUser = user;
                        houseUserList.add(currUser);
                        /*
                        disturb = findViewById(R.id.dnd);
                        if(MainActivity.currUser.busy==0){
                            disturb.setBackgroundColor(Color.RED);
                            alternate=1;
                        }else if(MainActivity.currUser.busy==1){
                            disturb.setBackgroundColor(Color.GREEN);
                            alternate=-1;
                        }*/

                        dndSwitch = findViewById(R.id.dnd_switch);
                        if (MainActivity.currUser.busy==0) {
                            dndSwitch.setChecked(false);
                            doNotDisturb = false;
                        } else if (MainActivity.currUser.busy==1) {
                            dndSwitch.setChecked(true);
                            doNotDisturb = true;
                        }

                        indexChop = MainActivity.currUser.userEmail.indexOf('@');
                        userChop = MainActivity.currUser.userEmail.substring(0, indexChop);
                        roomies.add(userChop);
                        System.out.println(indexChop);
                        System.out.println(userChop);
                        userFound = true;
                        Log.i(TAG, "onDataChange: user email=" + userEmail);
                        if (!(currUser.msgURL.equals(""))){
                            groupChatExists = true;
                            currUserHasURL = true;
                            groupChatURL = currUser.msgURL;
                        }
                    }
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if (!(user.name.equals(currUser.name)) && user.houseName.equals(currUser.houseName)) {
                        System.out.println(user.name);
                        if (user.msgURL != ""){
                            groupChatExists = true;
                            groupChatURL = user.msgURL;
                        }
                        if (groupChatExists){
                            Log.i(TAG, "onDataChange: groupChatExists="+groupChatExists);
                            Log.i(TAG, "onDataChange: user has URL="+currUserHasURL);
                            if (currUserHasURL == false){
                                currUser.msgURL = groupChatURL;
                                database.child("UserData").child(currUser.userEmail.substring(0,
                                        currUser.userEmail.indexOf('@'))).setValue(currUser);
                            }
                            user.msgURL = groupChatURL;
                        }
                        houseUserList.add(user);
                        roomies.add(user.userEmail.substring(0, user.userEmail.indexOf('@')));
                        counter += 1;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    //DO NOT DISTURB BUTTON
    public void Busy(View v){
        disturb = findViewById(R.id.dnd);
        alternate*=-1;
        if(alternate==-1){
            cs121.ucsc.roomie.User user = new User(MainActivity.currUser.name,
                    MainActivity.currUser.houseName,MainActivity.currUser.password,
                    MainActivity.currUser.houseAddress,1,MainActivity.currUser.userEmail,
                    MainActivity.currUser.msgURL, MainActivity.currUser.msgID);
            database.child("UserData").child(userChop).setValue(user);
            disturb.setBackgroundColor(Color.GREEN);
            Toast.makeText(cs121.ucsc.roomie.MainActivity.this, "Do Not Disturb Mode On",
                    Toast.LENGTH_SHORT).show();
        }else if(alternate==1){
            cs121.ucsc.roomie.User user = new User(MainActivity.currUser.name,
                    MainActivity.currUser.houseName,MainActivity.currUser.password,
                    MainActivity.currUser.houseAddress,0,MainActivity.currUser.userEmail,
                    MainActivity.currUser.msgURL,  MainActivity.currUser.msgID);
            database.child("UserData").child(userChop).setValue(user);
            disturb.setBackgroundColor(Color.RED);
            Toast.makeText(cs121.ucsc.roomie.MainActivity.this, "Do Not Disturb Mode Off",
                    Toast.LENGTH_SHORT).show();
        }
        listupdate();
    }
    */

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
        if(userFound) {
            String userId = currUser.name;
            //replace  spaces in username
            userId = userId.replaceAll("\\s", "");
            currUser.msgID = userId;
            String userNickname = userId;

            PreferenceUtils.setUserId(cs121.ucsc.roomie.MainActivity.this, userId);
            PreferenceUtils.setNickname(cs121.ucsc.roomie.MainActivity.this, userNickname);

            connectToSendBird(userId, userNickname);
        }
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
                currUser.msgID = user.getUserId();
                Log.i(TAG, "onConnected user ID: "+ user.getUserId());

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
