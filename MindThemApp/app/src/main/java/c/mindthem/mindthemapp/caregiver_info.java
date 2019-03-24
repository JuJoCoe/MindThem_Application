package c.mindthem.mindthemapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
public class caregiver_info extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private EditText firstname, lastname, phonenumber;
    private Button submitinfo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caregiver_information);

        firstname = (EditText) findViewById(R.id.firstname_field);
        lastname = (EditText) findViewById(R.id.lastname_field);
        phonenumber = (EditText) findViewById(R.id.phone_number);

        submitinfo = (Button) findViewById(R.id.submit_caregiver_info);

        mAuth = FirebaseAuth.getInstance();
        curUser = mAuth.getCurrentUser();

        submitinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == submitinfo){
                    SubmitCaregiverInfo();
                }
            }
        });
    }


    public boolean checkInputs() {

        // inputOk is true until an invalid input is found.
        boolean inputOK = true;

        if(TextUtils.isEmpty(firstname.getText())){
            firstname.setError("First name is required.");
            inputOK = false;
        }

        if(TextUtils.isEmpty(lastname.getText())){
            lastname.setError("Last name is required.");
            inputOK = false;
        }

        if(TextUtils.isEmpty(phonenumber.getText())){
            phonenumber.setError("Phone Number is required.");
            inputOK = false;
        } else if(phonenumber.getText().toString().length() < 10){
            phonenumber.setError("Not a valid phone number (not enough digits)");
            inputOK = false;
        }

        else if(phonenumber.getText().toString().length() >10 ){
            phonenumber.setError("Not a valid phone number (to many digits)");
            inputOK = false;
        }

        return inputOK;




    }



    private void SubmitCaregiverInfo(){
        boolean inputOK = checkInputs();
        if (inputOK) {

            firstname = (EditText) findViewById(R.id.firstname_field);
            lastname = (EditText) findViewById(R.id.lastname_field);
            phonenumber = (EditText) findViewById(R.id.phone_number);

            // Creates data entry Caregiver1 with child values
            DatabaseReference userDB = FirebaseDatabase.getInstance().getReference().child(curUser.getUid());
            String isprimarystring = "yes";
            DatabaseReference isprimary = userDB.child("Caregiver1").child("isprimary");
            DatabaseReference addfirstname = userDB.child("Caregiver1").child("firstname");
            DatabaseReference addlastname = userDB.child("Caregiver1").child("lastname");
            DatabaseReference addphonenumber = userDB.child("Caregiver1").child("phonenumber");

            isprimary.setValue(isprimarystring);
            addfirstname.setValue(firstname.getText().toString());
            addlastname.setValue(lastname.getText().toString());
            addphonenumber.setValue(phonenumber.getText().toString());

            Toast.makeText(caregiver_info.this, "Added Primary Caregiver",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), caregiver_info2.class);
            startActivity(intent);

            finish();


        }
    }


}

