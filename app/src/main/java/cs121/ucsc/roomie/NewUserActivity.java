package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;



public class NewUserActivity extends Activity {

    private static final String TAG ="DATA" ;

    EditText NameRegister;
    EditText UsernameRegister;
    EditText PasswordRegister;
    EditText HouseNameRegister;

    static String NameStore;
    static String UsernameStore;
    static String PasswordStore;
    static String HouseNameStore;

    static List<User> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        NameRegister = (EditText) findViewById(R.id.NameRegister);
        UsernameRegister = (EditText) findViewById(R.id.UsernameRegister);
        PasswordRegister = (EditText) findViewById(R.id.PasswordRegister);
        HouseNameRegister = (EditText) findViewById(R.id.houseName);



    }

    public void LoginReturn(View v){
        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }

    public void Return(View v){
        this.NameStore = NameRegister.getText().toString();
        this.UsernameStore = UsernameRegister.getText().toString();
        this.PasswordStore = PasswordRegister.getText().toString();
        this.HouseNameStore = HouseNameRegister.getText().toString();


        User u = new User(NameStore, UsernameStore, PasswordStore, HouseNameStore);
        userList.add(u);
        saveUserList(userList);

        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }
    public void saveUserList(List<User> u) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(u);

        editor.putString(TAG, json);

        Toast.makeText(this,"stored!",Toast.LENGTH_LONG).show();
        editor.commit();
    }
}
