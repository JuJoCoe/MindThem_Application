package c.mindthem.mindthemapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;

/**
 * Created by jordan on 3/5/2019.
 */

public class bluetooth_test extends Activity {
    Bluetooth bluetooth;
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    public String bothnumbers = "test2";
    public String firstname = "test3";
    public String firstnumber = "test";
    public String secondnumber = "test1";
    public String comma = ",";
    public String contact = "contact";
    ImageView BluetoothPic;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_test);

        bluetooth = new Bluetooth(this);
        BluetoothPic = (ImageView) findViewById(R.id.BluetoothImage);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();



        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
        DatabaseReference sendfirstnumber = userDB.child("Caregiver1").child("phonenumber");
        sendfirstnumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstnumber = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference sendsecondnumber = userDB.child("Caregiver2").child("phonenumber");
        sendsecondnumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                secondnumber = (String) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference sendfirstname = userDB.child("Caregiver1").child("firstname");
        sendfirstname.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstname = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Button bluetoothconnection = (Button) findViewById(R.id.connectbutton);
        bluetoothconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connecttobluetooth();
            }
        });

        Button sendinfobutton = (Button) findViewById(R.id.sendinfobutton);
        sendinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendinfo();
            }
        });

        Button disconnectbutton = (Button) findViewById(R.id.disconnectbutton);
        disconnectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disconnectbluetooth();
            }
        });

        Button manualdetection = (Button) findViewById(R.id.Manual_Detection);
        manualdetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualDetection(view);
            }
        });

        Button bypasstocaregiver = (Button) findViewById(R.id.Change_Info);
        bypasstocaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCaregiver(view);
            }
        });


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

    }


    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        bluetooth.enable();
        bluetooth.connectToName("raspi21");


    }

    public void ConfirmConnected(){
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connected to raspi21", Toast.LENGTH_LONG).show();
            }

            });
        BluetoothPic.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }

    public void ConfirmDisconnected(){
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Disconnected to raspi21", Toast.LENGTH_LONG).show();
            }

        });
        BluetoothPic.setColorFilter(getResources().getColor(R.color.Inactive));
    }

    public void FailureMessage(){
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.disconnect();
        bluetooth.onStop();
    }


    private void sendinfo() {
        bothnumbers = contact + comma + firstname + comma + firstnumber + comma + secondnumber;
        bluetooth.send(bothnumbers);
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

    public void ContinueToMain(View v){
        Intent intent = new Intent(bluetooth_test.this, logs_alert_page.class);
        startActivity(intent);
    }

    public void ManualDetection(View v){
        String detect = "detect";
        bluetooth.send("detect");
    }

    public void ChangeCaregiver(View v){
        Intent intent = new Intent(bluetooth_test.this, caregiver_info.class);
        startActivity(intent);
    }





}
