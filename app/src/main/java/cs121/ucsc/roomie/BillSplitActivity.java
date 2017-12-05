package cs121.ucsc.roomie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BillSplitActivity extends AppCompatActivity {

    static String appId = "JUstin-Seo-5";
    static String appName = "VenmoTest";
    static String recipient = "Kevin-Li-66";
    static String amount = "1";
    static String note = "fuck";
    static String txn = "pay";
    private int REQUEST_CODE_VENMO_APP_SWITCH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_split);
    }

    public void VenmoGo(View v){
        Intent venmoIntent = cs121.ucsc.roomie.VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
        startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
    }
}
