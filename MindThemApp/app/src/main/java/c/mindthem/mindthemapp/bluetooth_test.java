package c.mindthem.mindthemapp;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    public String bothnumbers = "";
    public String firstname = "firstname";
    public String firstname2 = "firstname2";
    public String firstnumber = "firstnumber";
    public String secondnumber = "secondnumber";
    public String comma = ",";
    public String contact = "contact";
    public String minstring = "min";
    public String maxstring = "max";
    public String fulldetection = "";
    boolean inputOK = true;
    public String isprimary ="";
    private EditText min, max;
    ImageView BluetoothPic;
    ArrayList<String> listItems = new ArrayList<String>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_test);

        bluetooth = new Bluetooth(this);
        BluetoothPic = (ImageView) findViewById(R.id.BluetoothImage);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        min = (EditText) findViewById(R.id.Min);
        max = (EditText) findViewById(R.id.Max);


        //Gets the data from the database and adds it to the strings firstnumber, secondnumber, firstname
        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
        DatabaseReference sendfirstnumber = userDB.child("Caregiver1").child("phonenumber");
        DatabaseReference checkprimary = userDB.child("Caregiver1").child("isprimary");

        checkprimary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isprimary = (String) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        DatabaseReference sendfirstname2 = userDB.child("Caregiver2").child("firstname");
        sendfirstname2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                firstname2 = (String) dataSnapshot.getValue();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference gettimestamp = userDB.child("Timestamps");

        gettimestamp.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tempstring = (String) snapshot.getValue();
                    listItems.add(tempstring);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Intialize all buttons and links them to methods
        Button bluetoothconnection = (Button) findViewById(R.id.connectbutton);
        bluetoothconnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connecttobluetooth();
            }
        });

        Button stopdetectionbutton = (Button) findViewById(R.id.stopdetection);
        stopdetectionbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StopDetection();
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
                ManualDetection();
            }
        });

        Button changecaregiver = (Button) findViewById(R.id.Change_Info);
        changecaregiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeCaregiver(view);
            }
        });

        Button viewlog = (Button) findViewById(R.id.viewlog);
        viewlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueToMain(view);
            }
        });


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
                SendAlerts(message);
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


    //method runs when activity is started
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
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connected to raspi21", Toast.LENGTH_LONG).show();
            }

            });
        BluetoothPic.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
        sendinfo();
    }

    public void ConfirmDisconnected(){
        bluetooth_test.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Disconnected from raspi21", Toast.LENGTH_LONG).show();
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

    private void sendinfo() {
        if(isprimary.equals("yes")){
            bothnumbers = contact + comma + firstname + comma + firstnumber + comma + secondnumber;
            bluetooth.send(bothnumbers);
        }else{
            bothnumbers = contact + comma + firstname2 + comma + secondnumber + comma + firstnumber;
            bluetooth.send(bothnumbers);
        }
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

    public void ManualDetection(){
        boolean inputOK = checkInputs();
        if(inputOK) {
            minstring = min.getText().toString();
            maxstring = max.getText().toString();
            String detect = "detect";

            fulldetection = detect + comma + maxstring + comma + minstring;
            bluetooth.send(fulldetection);
            bluetooth_test.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Manual Detection Started", Toast.LENGTH_LONG).show();
                }

            });
        }
    }

    public void StopDetection(){
        bluetooth.send("stop,");
    }

    public void ChangeCaregiver(View v){
        Intent intent = new Intent(bluetooth_test.this, caregiver_info.class);
        startActivity(intent);
    }

    public boolean checkInputs(){
        if(TextUtils.isEmpty(min.getText())){
            min.setError("Number is required.");
            inputOK = false;
        }

        if(TextUtils.isEmpty(max.getText())){
            max.setError("Number is required.");
            inputOK = false;
        }
        return inputOK;

    }

    public void SendAlerts(String message){
        String s = message;
        String[] tokens = s.split(",");

        for (String t : tokens)
            listItems.add(t);

        DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
        DatabaseReference addtimestamp = userDB.child("Timestamps");

        addtimestamp.addListenerForSingleValueEvent(new ValueEventListener() {
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            String tempstring = (String) snapshot.getValue();
            listItems.add(tempstring);
        }
    }
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
    }
});
        addtimestamp.setValue(listItems);

    }

}
