package cs121.ucsc.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cs121.ucsc.roomie.R;

public class BillSplitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_split);
    }

    public void VenmoGo(View v){
        System.out.println("fuckme");
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.venmo");
        if (launchIntent != null) {
            System.out.println("fuckmee");
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }



}
