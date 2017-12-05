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

    public void RequestGo(View v){
        Intent intent = new Intent(this, RequestActivity.class);
        startActivity(intent);
    }

    public void PayGo(View v){
        Intent intent = new Intent(this, PayActivity.class);
        startActivity(intent);
    }
}
