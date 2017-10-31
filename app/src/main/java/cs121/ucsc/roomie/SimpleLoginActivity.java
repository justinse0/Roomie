package cs121.ucsc.roomie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SimpleLoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);
        final Button login =  findViewById(R.id.login);
        final Button newuser =  findViewById(R.id.newaccount);

        final EditText account = findViewById(R.id.account);
        final EditText password = findViewById(R.id.password);




    }
}
