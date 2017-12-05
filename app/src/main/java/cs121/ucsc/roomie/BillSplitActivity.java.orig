package cs121.ucsc.roomie;

import android.content.Intent;
import android.content.pm.PackageManager;
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
<<<<<<< HEAD
        Intent venmoIntent = cs121.ucsc.roomie.VenmoLibrary.openVenmoPayment(appId, appName, recipient, amount, note, txn);
        startActivityForResult(venmoIntent, REQUEST_CODE_VENMO_APP_SWITCH);
    }
=======
        System.out.println("fuckme");
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.venmo");
        if (launchIntent != null) {
            System.out.println("fuckmee");
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }



>>>>>>> adcee9f4f4ee2432fdc497e4516b51bd079f081c
}
