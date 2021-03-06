package cs121.ucsc.roomie;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class MessageActivity extends AppCompatActivity {
   // private ChannelHandler openHandler;
    //private ConnectionHandler connectionHandler;
    private List<User> roomies;


    public static String text_1 = "";
    private static ArrayList<String> messageItems;
    ListView listView;
    public String yearString = new String("");
    private User currUser;
    private int x = 0;
    static String[] listString;

    public static String[] staticNames;
    //for the demo
    private ArrayList<String> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] staticNames = {"John: Who wants to go out for dinner tonight",
                "Olivia: Sorry, I have  a Midterm tomorrow :/",
                "Alan: I'm down! Let's go to Pericos",
                "John: Sweet, I'll be home at 7:00"};
        listString = new String[0];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
      //  roomies = SimpleLoginActivity.getUsers();
        final String username = getIntent().getStringExtra("CurrentUser");

        userList = new ArrayList<String>();
        for(int i=0; i<4; i++){

            userList.add(staticNames[i]);

        }
        //create the button, listview, and textfield assignments
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final EditText message = (EditText) findViewById(R.id.messageText);
        messageItems = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.ListView);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(cs121.ucsc.roomie.MessageActivity.this, android.R.layout.simple_expandable_list_item_1, userList);
        listView.setAdapter(listAdapter);

        //this.PopulateList( x, listString);



        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //open output file
    //            Intent intent = new Intent(MessageActivity.this, MainActivity.class);
                try {
                    File file = getFileStreamPath("messageFile.txt");
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    FileOutputStream messageFile = openFileOutput(file.getName(), Context.MODE_APPEND);
                    BufferedOutputStream writer = new BufferedOutputStream(messageFile);

                    //write data string to file
                    //THIS IS WHERE I NEED THE USER'S NAME (CAPS FOR VISIBILITY)
                    text_1 = username + message.getText().toString();
                    String string = text_1;
                    writer.write(string.getBytes());


       //             Bundle bundle = new Bundle();
      //              intent.putExtras(bundle);

                    //open new file
                    writer.flush();
                    writer.close();
                    //PopulateList(x, listString);
                }catch (Exception e){
                    e.printStackTrace();
                }
                message.setText("");
            }
        });


    }



    //populate  the list. I need to be able to refresh it every time somebody sends a message
    private void PopulateList( int counter, String[] strArray ){
        try {
            counter  = 0;
            FileInputStream messageFile = openFileInput("messageFile.txt");
            DataInputStream in = new DataInputStream(messageFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String inputString = "";
            while ((inputString = br.readLine()) != null) {
                messageItems.add(inputString);
                counter += 1;
            }
            strArray = new String[counter];
            for(int i = 0; i<counter; i++){
                strArray[i] = messageItems.get(i);
            }
            messageFile.close();
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(cs121.ucsc.roomie.MessageActivity.this, android.R.layout.simple_expandable_list_item_1, listString);
            listView.setAdapter(listAdapter);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
/*
    @Override
    public void onPause(){
        super.onPause();
        SendBird.removeChannelHandler("openHandler");
        SendBird.removeConnectionHandler(UNIQUE_HANDLER_ID);
    }

    SendBird.addChannelHandler(UNIQUE_HANDLER_ID, new SendBird.ChannelHandler() {
        @Override
        public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
            // Received a chat message.
        }

        /*@Override
        public void onMessageDeleted(BaseChannel baseChannel, long messageId) {
            // When a message has been deleted.
        }

        @Override
        public void onChannelChanged(BaseChannel baseChannel) {
            // When a channel property has been changed.
            // More information on the properties can be found below.
        }

        @Override
        public void onChannelDeleted(String channelUrl, BaseChannel.ChannelType channelType) {
            // When a channel has been deleted.
        }
*/
/*
        @Override
        public void onReadReceiptUpdated(GroupChannel groupChannel) {
            // When read receipt has been updated.
        }

        @Override
        public void onTypingStatusUpdated(GroupChannel groupChannel) {
            // When typing status has been updated.
        }
        */
/*
        @Override
        public void onUserJoined(GroupChannel groupChannel, User user) {
            // When a new member joined the group channel.
        }

        @Override
        public void onUserLeft(GroupChannel groupChannel, User user) {
            // When a member left the group channel.
        }
*//*
        @Override
        public void onUserEntered(OpenChannel openChannel, User user) {
            // When a new user entered the open channel.
        }

        @Override
        public void onUserExited(OpenChannel openChannel, User user) {
            // When a new user left the open channel.
        }
        */
/*
        @Override
        public void onUserMuted(OpenChannel openChannel, User user) {
            // When a user is muted on the open channel.
        }

        @Override
        public void onUserUnmuted(OpenChannel openChannel, User user) {
            // When a user is unmuted on the open channel.
        }
*/
/*
        @Override
        public void onUserBanned(OpenChannel openChannel, User user) {
            // When a user is banned on the open channel.
        }

        @Override
        public void onUserUnbanned(OpenChannel openChannel, User user) {
            // When a user is unbanned on the open channel.
        }

        @Override
        public void onChannelFrozen(OpenChannel openChannel) {
            // When the open channel is frozen.
        }

        @Override
        public void onChannelUnfrozen(OpenChannel openChannel) {
            // When the open channel is unfrozen.
        }
    });
*/
/*
    SendBird.connect(USER_ID, new SendBird.ConnectHandler(){
        @Override
        public void onConnected (User user, SendBirdException e){
        if (e != null) {
            //error
            return;
        }
    }
    });

    OpenChannel.createChannel(new OpenChannel.OpenChannelCreateHandler() {
        @Override
        public void onResult(OpenChannel openChannel, SendBirdException e) {
            if (e != null) {
                // Error.
                return;
            }
        }
    });

    OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
        @Override
        public void onResult(OpenChannel openChannel, SendBirdException e) {
            if (e != null) {
                // Error!
                return;
            }

            // Successfully fetched the channel.
            // Do something with openChannel.
            openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                @Override
                public void onResult(SendBirdException e) {
                    if (e != null) {
                        // Error.
                        return;
                    }
                }
            });
        }
    });

    //to go in an onClick listener perhaps?
    channel.sendUserMessage(MESSAGE, DATA, CUSTOM_TYPE, new BaseChannel.SendUserMessageHandler() {
        @Override
        public void onSent(UserMessage userMessage, SendBirdException e) {
            if (e != null) {
                // Error.
                return;
            }
        }
    });

    //use this to find participants in the group chat
    UserListQuery userListQuery = openChannel.createParticipantListQuery();
    userListQuery.next(new UserListQuery.UserListQueryResultHandler() {
        @Override
        public void onResult(List<User> list, SendBirdException e) {
            if (e != null) {
                // Error.
                return;
            }
        }
    });

    //tools to maintain the connection to the server
    SendBird.addConnectionHandler(UNIQUE_HANDLER_ID, new SendBird.ConnectionHandler() {
        @Override
        public void onReconnectStarted() {
            // Network has been disconnected. Auto reconnecting starts.
        }

        @Override
        public void onReconnectSucceeded() {
            // Auto reconnecting succeeded.
        }

        @Override
        public void onReconnectFailed() {
            // Auto reconnecting failed. You should call `connect` to reconnect to SendBird.
        }
    });
*/}

