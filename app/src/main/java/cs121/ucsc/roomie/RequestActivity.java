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


public class RequestActivity extends Activity {
    ListView listView;
    EditText editTextView;
    ArrayList<RequestModel> ItemModelList;
    RequestCustomAdapter customAdapter;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.listview2);
        editTextView = (EditText) findViewById(R.id.editTextView2);
        ItemModelList = new ArrayList<RequestModel>();


        ItemModelList = fetch();

        customAdapter = new RequestCustomAdapter(getApplicationContext(), ItemModelList);

        listView.setEmptyView(findViewById(R.id.listview2));
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();

    }
    public ArrayList<RequestModel> fetch(){
        final ArrayList<RequestModel> list = new ArrayList();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("RequestData").child(MainActivity.currUser.name);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
                    for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                        RequestModel tdm = postSnapshot.getValue(RequestModel.class);
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
            RequestModel md = new RequestModel(name);
            ItemModelList.add(md);
            database.child("RequestData").child(MainActivity.currUser.name).setValue(ItemModelList);
            customAdapter.notifyDataSetChanged();
            editTextView.setText("");
        }
    }
}
