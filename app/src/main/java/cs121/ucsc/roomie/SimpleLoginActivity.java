package cs121.ucsc.roomie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;


public class SimpleLoginActivity extends Activity implements Observer, Button.OnClickListener{

    private Model model;
    private Button login;
    private Button newuser;
    private EditText account;
    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_login);
        login = (Button)findViewById(R.id.login);
        newuser = (Button) findViewById(R.id.newaccount);

        account = (EditText) findViewById(R.id.account);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(this);
        newuser.setOnClickListener(this);

        model = new Model();
        model.addObserver(this);




    }

    @Override
    public void update(Observable observable, Object o){

    }
    //LOGIN
    //fetch login data (gettext)
    //compare user input with database (if ==1)
    //if match, direct to main page
    //else do nothing, toast user not found / password/account incorrect

    //NEWACCOUNT
    //direct to NewUserActivity that ask user to create account / get basic info
    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.login:
                if((model.SearchForAccount(account.getText().toString())  == 1) && (model.SearchForPassword(password.getText().toString()) == 1))
                {
                    model.ToMainScreen();
                }
                break;
            case R.id.newaccount:
                model.ToNewUser();
                break;
        }
    }
}
