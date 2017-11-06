package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;


public class SimpleLoginActivity extends Activity {


    private Button login;

    private EditText account;
    private EditText password;

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
}

