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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class NewUserActivity extends Activity {



    EditText NameRegister;
    EditText UsernameRegister;
    EditText PasswordRegister;
    EditText HouseNameRegister;
    EditText HouseAddressRegister;

    static String NameStore;
    static String UsernameStore;
    static String PasswordStore;
    static String HouseNameStore;
    static String HouseAddressStore;
    static boolean HouseExistsStore;
    static String ChoppedUser;
    //firebase
    FirebaseAuth mAuth;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        NameRegister = (EditText) findViewById(R.id.NameRegister);
        UsernameRegister = (EditText) findViewById(R.id.UsernameRegister);
        PasswordRegister = (EditText) findViewById(R.id.PasswordRegister);
        HouseNameRegister = (EditText) findViewById(R.id.houseName);
        HouseAddressRegister = (EditText) findViewById(R.id.houseAddress);
        HouseExistsStore = false;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void LoginReturn(View v){
        Intent intent = new Intent(this, SimpleLoginActivity.class);
        startActivity(intent);
    }
    private void writeNewUser(){
        final OpenChannelStore openChannelStore = new OpenChannelStore( HouseNameStore);
        cs121.ucsc.roomie.User user = new User(NameStore,HouseNameStore,PasswordStore,HouseAddressStore,0,UsernameStore, "","");
        database.child("UserData").child(ChoppedUser).setValue(user);
        database.child("OpenHouse").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    OpenChannelStore holder = snapshot.getValue(OpenChannelStore.class);
                    if ((holder.houseName.equals(HouseNameStore))){
                        HouseExistsStore = true;
                    }
                }
                if(!HouseExistsStore) {
                    database.child("OpenHouse").child(HouseNameStore).setValue(openChannelStore);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //database.child("OpenHouse").child(HouseNameStore).setValue(openChannelStore);

    }
    public void Return(View v){

        this.NameStore = NameRegister.getText().toString();
        this.UsernameStore = UsernameRegister.getText().toString();
        this.PasswordStore = PasswordRegister.getText().toString();
        this.HouseNameStore = HouseNameRegister.getText().toString();
        this.HouseAddressStore = HouseAddressRegister.getText().toString();
        int index = UsernameStore.indexOf('@');
        ChoppedUser = UsernameStore.substring(0,index);

        mAuth.createUserWithEmailAndPassword(UsernameStore, PasswordStore)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(cs121.ucsc.roomie.NewUserActivity.this, "Authentication succeed",
                                    Toast.LENGTH_SHORT).show();
                            writeNewUser();
                            SuccessReturn();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(cs121.ucsc.roomie.NewUserActivity.this, "Authentication failed.",
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
