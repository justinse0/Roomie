package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class SimpleLoginActivity extends Activity {


    private static final String TAG ="DATA" ;
    private Button login;

    private EditText account;
    private EditText password;

    private String nAcc;
    private String nPass;
    private List<User> u ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);
        login = (Button) findViewById(R.id.login);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);

    }

    public void GoToNewUser(View view){
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }

    public void Login(View view){
        this.nAcc = account.getText().toString();
        this.nPass = password.getText().toString();

        u = getSavedUser();

        for(int i = 0; i<u.size();i++){
            if(u.get(i).username.equals(nAcc) && u.get(i).password.equals(nPass)){
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(this,"Incorrect account or password!",Toast.LENGTH_LONG).show();
                break;
            }
        }

    }
    public List<User> getSavedUser(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, null);
        Type type = new TypeToken<ArrayList<User>>() {}.getType();
        ArrayList<User> arrayList = gson.fromJson(json, type);
        return arrayList;
    }
}

