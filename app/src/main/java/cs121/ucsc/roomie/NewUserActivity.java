package cs121.ucsc.roomie;

import android.app.Activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class NewUserActivity extends Activity {



    EditText NameRegister;
    EditText UsernameRegister;
    EditText PasswordRegister;
    EditText HouseNameRegister;

    static String NameStore;
    static String UsernameStore;
    static String PasswordStore;
    static String HouseNameStore;


    //firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        NameRegister = (EditText) findViewById(R.id.NameRegister);
        UsernameRegister = (EditText) findViewById(R.id.UsernameRegister);
        PasswordRegister = (EditText) findViewById(R.id.PasswordRegister);
        HouseNameRegister = (EditText) findViewById(R.id.houseName);

        mAuth = FirebaseAuth.getInstance();
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
        System.out.println(UsernameStore);

        mAuth.createUserWithEmailAndPassword(UsernameStore, PasswordStore)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(NewUserActivity.this, "Authentication succeed",
                                    Toast.LENGTH_SHORT).show();
                            SuccessReturn();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(NewUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });



    }
    public void SuccessReturn(){
        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }


}
