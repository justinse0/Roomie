package cs121.ucsc.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BillSplitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_split);
    }

    public void RequestGo(View v){
        startActivity(new Intent(BillSplitActivity.this, RequestActivity.class));
    }

    public void PayGo(View v){
        startActivity(new Intent(BillSplitActivity.this, PayActivity.class));
    }
}
