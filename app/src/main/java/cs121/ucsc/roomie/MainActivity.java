package cs121.ucsc.roomie;
import android.app.Activity;
import android.graphics.PorterDuff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private final String APPID = new String("A21A2BDC-6192-4A27-A4A9-F3FEA23F2CFA");
    private ArrayList<String> roomies;
    private ListView listView;
    static ArrayList<User> houseUserList;
    private static User currUser;
    private String userPass;
    private boolean secondPress;
    private String[] userArray;
    Button displayMates ;
    Button goTodo;
    Button messageStart;
    Button splitBill;
    public static String[] staticNames;
    FirebaseAuth mAuth;
    DatabaseReference database;
    private int counter;

    public User getUser(){
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
       // SendBird.init(APPID, this);

        //create the list and buttons for displaying roommates
        //and find the current user
        listView = (ListView) findViewById(R.id.roomieList);
        displayMates = (Button) findViewById(R.id.roomies);
        goTodo = (Button) findViewById(R.id.todo);
        splitBill = (Button) findViewById(R.id.billSplit);
        //messageStart = (Button) findViewById(R.id.messaging);
        secondPress = false;

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
                                houseUserList.add(currUser);
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
                Intent intent = new Intent((MainActivity.this), BillSplitActivity.class);
                startActivity(intent);
            }
        });
        goTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, ToDoActivity.class);
               // intent.putExtra("CurrentUser", currUser.userPass);
                startActivity(intent);
            }
        });

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







}
