package cs121.ucsc.roomie;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewUserActivity extends Activity {

    EditText NameRegister;
    EditText UsernameRegister;
    EditText PasswordRegister;

    static String NameStore;
    static String UsernameStore;
    static String PasswordStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        NameRegister = (EditText) findViewById(R.id.NameRegister);
        UsernameRegister = (EditText) findViewById(R.id.UsernameRegister);
        PasswordRegister = (EditText) findViewById(R.id.PasswordRegister);

    }

    public void LoginReturn(View v){
        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }

    public void Return(View v){
        this.NameStore = NameRegister.getText().toString();
        this.UsernameStore = UsernameRegister.getText().toString();
        this.PasswordStore = PasswordRegister.getText().toString();
        Toast.makeText(this,"Registered!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }
}
