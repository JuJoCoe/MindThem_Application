package c.mindthem.mindthemapp;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.BluetoothCallback;
import me.aflak.bluetooth.DeviceCallback;
import me.aflak.bluetooth.DiscoveryCallback;

public class BluetoothService extends Service {
    Bluetooth bluetooth;

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();
        bluetooth = new Bluetooth(this);
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
             //   ConfirmConnected();
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device, String message) {
             //   ConfirmDisconnected();
            }

            @Override
            public void onMessage(String message) {
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onConnectError(BluetoothDevice device, String message) {
             //   FailureMessage();
            }
        });

    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        // Do your Bluetooth Work Here
        Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
        bluetooth.onStart();
        bluetooth.enable();
        bluetooth.connectToName("raspi21");

    }
    public void testmethod(String test) {
        bluetooth.send(test);
    }


}
