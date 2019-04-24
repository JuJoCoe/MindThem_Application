package c.mindthem.mindthemapp;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;



public class logs_alert_page extends AppCompatActivity {

    Bluetooth bluetooth;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;

    private DrawerLayout drawerLayout;

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int detecttracker = 2;
    int logtracker = 0;
    String slogtracker = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_alert);

        //bluetooth and firebase intialization
        bluetooth = new Bluetooth(this);
        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        //Displays Alert Log
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);
                if (position % 2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.argb(255, 255, 255, 255));
                } else {
                    // Set the background color for alternate row/item
                    view.setBackgroundColor(Color.argb(255, 180, 189, 242));
                }
                return view;
            }
        };
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        //Displays hamburger header
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.menuimage);
        drawerLayout = findViewById(R.id.drawer_layout);

        //Displays hamburger toolbar menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        if(menuItem.getItemId() == R.id.testing){
                            Intent intent = new Intent(logs_alert_page.this, bluetooth_test.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.change_caregiver){
                            Intent intent = new Intent(logs_alert_page.this, caregiver_info.class);
                            startActivity(intent);
                        }
                        if(menuItem.getItemId() == R.id.switch_caregivers){
                            Toast.makeText(logs_alert_page.this, "Button Functionality Coming Soon",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(menuItem.getItemId() == R.id.account_settings){
                            Intent intent = new Intent(logs_alert_page.this, change_password.class);
                            startActivity(intent);
                        }
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        drawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

        //Populates alert log with Firebase
        //Get Database Reference
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
        DatabaseReference displaytimestamp = userDB.child("Timestamps");
        displaytimestamp.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tempstring = (String) snapshot.getValue();
                    listItems.add(tempstring);
                    adapter.notifyDataSetChanged();
                }
            }
             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
        });


/*
        //Initialize bluetooth methods
        bluetooth.setBluetoothCallback(new BluetoothCallback() {
            @Override
            public void onBluetoothTurningOn() {
            }

            @Override
            public void onBluetoothOn() {
                Toast.makeText(getApplicationContext(), "Enabled Bluetooth", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onBluetoothTurningOff() {
            }

            @Override
            public void onBluetoothOff() {
            }

            @Override
            public void onUserDeniedActivation() {
                // when using bluetooth.showEnableDialog()
                // you will also have to call bluetooth.onActivityResult()
            }
        });

        bluetooth.setDiscoveryCallback(new DiscoveryCallback() {
            @Override
            public void onDiscoveryStarted() {
            }

            @Override
            public void onDiscoveryFinished() {
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
            }

            @Override
            public void onError(String message) {
            }
        });

        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                ConfirmConnected();
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
                ConfirmDisconnected();
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
                FailureMessage();
            }
        });
*/
        //end of on create
    }


    //Adds Alert to Firebase
    public void addItems(View v) {
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
        listItems.add(getCurrentTimeStamp());
        adapter.notifyDataSetChanged();
        DatabaseReference addtimestamp = userDB.child("Timestamps");
        addtimestamp.setValue(listItems);

    }

    public static String getCurrentTimeStamp() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy, hh:mm a");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Selects item in the hamburger menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //method runs when activity is started
    /*
    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        bluetooth.enable();
        bluetooth.connectToName("raspi21");
    }

    //method runs when activity is ended
    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.disconnect();
        bluetooth.onStop();
    }

    public void ConfirmConnected(){
        logs_alert_page.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connected to raspi21", Toast.LENGTH_LONG).show();
            }

        });
  //      BluetoothPic.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
     //   sendinfo();
    }

    public void ConfirmDisconnected(){
        logs_alert_page.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Disconnected from raspi21", Toast.LENGTH_LONG).show();
            }

        });
    //    BluetoothPic.setColorFilter(getResources().getColor(R.color.Inactive));
    }

    public void FailureMessage(){
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }

        });
    }

    private void connecttobluetooth() {
        if(!bluetooth.isConnected()) {
            bluetooth.connectToName("raspi21");
        }
    }

    private void disconnectbluetooth() {
        if(bluetooth.isConnected()) {
            bluetooth.disconnect();
        }
    }
*/
 /*   private void sendinfo() {
        if(isprimary.equals("yes")){
            bothnumbers = contact + comma + firstname + comma + firstnumber + comma + secondnumber;
            bluetooth.send(bothnumbers);
        }else{
            bothnumbers = contact + comma + firstname2 + comma + secondnumber + comma + firstnumber;
            bluetooth.send(bothnumbers);
        }
    }
    */

}

