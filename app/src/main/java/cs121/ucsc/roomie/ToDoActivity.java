package cs121.ucsc.roomie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ToDoActivity extends Activity {
    ListView listView;
    EditText editTextView;
    ArrayList<ToDoModel> ItemModelList;
    ToDoCustomAdapter customAdapter;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.listview);
        editTextView = (EditText) findViewById(R.id.editTextView);
        ItemModelList = new ArrayList<ToDoModel>();


        ItemModelList = fetch();

        customAdapter = new ToDoCustomAdapter(getApplicationContext(), ItemModelList);

        listView.setEmptyView(findViewById(R.id.listview));
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }
    public ArrayList<ToDoModel> fetch(){
        final ArrayList<ToDoModel> list = new ArrayList();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("ToDoData").child(MainActivity.currUser.houseName);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        ToDoModel tdm = postSnapshot.getValue(ToDoModel.class);
                        System.out.println(tdm.getName());
                        list.add(tdm);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return list;
    }
    @SuppressLint("NewApi")
    public void addValue(View v) {
        String name = editTextView.getText().toString();
        if (name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Plz enter Values",
                    Toast.LENGTH_SHORT).show();
        } else {
            ToDoModel md = new ToDoModel(name);
            ItemModelList.add(md);
            database.child("ToDoData").child(MainActivity.currUser.houseName).setValue(ItemModelList);
            customAdapter.notifyDataSetChanged();
            editTextView.setText("");
        }
    }
}
