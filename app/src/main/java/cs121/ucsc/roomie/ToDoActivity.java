package cs121.ucsc.roomie;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ToDoActivity extends Activity {
    ListView listView;
    EditText editTextView;
    ArrayList<ToDoModel> ItemModelList;
    ToDoCustomAdapter customAdapter;
    FirebaseAuth mAuth;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) findViewById(R.id.listview);
        editTextView = (EditText) findViewById(R.id.editTextView);
        ItemModelList = new ArrayList<ToDoModel>();
        customAdapter = new ToDoCustomAdapter(getApplicationContext(), ItemModelList);
        listView.setEmptyView(findViewById(R.id.listview));
        listView.setAdapter(customAdapter);
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
