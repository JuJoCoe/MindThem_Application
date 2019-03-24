package c.mindthem.mindthemapp;

import android.app.ListActivity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;



public class logs_alert_page extends ListActivity {

    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int detecttracker = 2;

    Bluetooth bluetooth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_alert);

        bluetooth = new Bluetooth(this);

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
        setListAdapter(adapter);


        bluetooth.setDeviceCallback(new DeviceCallback() {
            @Override
            public void onDeviceConnected(BluetoothDevice device) {
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
            }
        });

    }


    public void addItems(View v) {
        Button Notify = (Button) findViewById(R.id.AlertButton);

        //Positive Alert
        if (detecttracker == 2) {
            listItems.add(getCurrentTimeStamp());
            Notify.setText(R.string.alert_true);
            Notify.setBackground(getDrawable(R.drawable.detection_button));
            detecttracker--;
            detecttracker--;
            bluetooth.send("detect");
        }
        //No Alert
        else if (detecttracker == 1) {
            Notify.setText(R.string.alert_false);
            Notify.setBackground(getDrawable(R.drawable.no_detection_button));
            detecttracker++;
            //Inactive
        } else {
            Notify.setText(R.string.alert_inactive);
            Notify.setBackground(getDrawable(R.drawable.inactive_button));
            detecttracker++;
            detecttracker++;
        }


        adapter.notifyDataSetChanged();
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

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        bluetooth.enable();
        bluetooth.connectToName("raspi21");
    //    bluetooth.send("test");
    }

    protected void onStop() {
        super.onStop();
        bluetooth.disconnect();
        bluetooth.onStop();
    }









}

