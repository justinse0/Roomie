package cs121.ucsc.roomie;

import android.os.Bundle;

import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.widget.TextView;



public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Menu menu;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //textview
        tv1 = (TextView)findViewById(R.id.dym_name);
        tv1.setText(MainActivity.currUser.name);
        tv2 = (TextView)findViewById(R.id.dym_email);
        tv2.setText(MainActivity.currUser.userEmail);
        tv3 = (TextView)findViewById(R.id.dym_housename);
        tv3.setText(MainActivity.currUser.houseName);
        tv4 = (TextView)findViewById(R.id.dym_address);
        tv4.setText(MainActivity.currUser.houseAddress);
        tv5 = (TextView)findViewById(R.id.dym_busy);
        if(MainActivity.currUser.busy == 1){
            tv5.setText("Yes, busy");
        }else{
            tv5.setText("No, not busy");
        }
        //
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //update current user header
        View hView = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) hView.findViewById(R.id.headeremail);
        nav_user.setText(MainActivity.currUser.userEmail);
        TextView nav_name = (TextView) hView.findViewById(R.id.headername);
        nav_name.setText(MainActivity.currUser.name);
        //

        menu = navigationView.getMenu();
        for(int i = 0; i<MainActivity.houseUserList.size(); i++){
            menu.add(MainActivity.houseUserList.get(i).name);

        }

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        String name = item.toString();

        for(int i = 0; i<MainActivity.houseUserList.size(); i++){
            if(name.equals(MainActivity.houseUserList.get(i).name)){
                tv1.setText(MainActivity.houseUserList.get(i).name);
                tv2.setText(MainActivity.houseUserList.get(i).userEmail);
                tv3.setText(MainActivity.houseUserList.get(i).houseName);
                tv4.setText(MainActivity.houseUserList.get(i).houseAddress);
            }
            if(MainActivity.houseUserList.get(i).busy == 1){
                tv5.setText("Busy");
            }
            else{
                tv5.setText("No, not Busy");
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
