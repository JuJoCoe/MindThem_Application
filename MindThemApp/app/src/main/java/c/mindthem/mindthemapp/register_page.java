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



import java.util.Set;

/**
 * Created by jorda on 2/21/2019.
 */

public class register_page extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private EditText password, email, confirmPassword, name;
    private Button button_register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        email = (EditText) findViewById(R.id.email_field);
        password =(EditText) findViewById(R.id.password_field);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_field);
        name = (EditText) findViewById(R.id.name_field);

        button_register = (Button)findViewById(R.id.create_account_button);
        mAuth = FirebaseAuth.getInstance();

        //Register button on click
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_register){
                    RegisterUser();
                }
            }
        });
    }

    public void addUserToDatabase(){

        // Get a reference to the root of the Firebase database.
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // Create the new child node in the database for the user, using the unique id generated
        // by the Firebase authentifier.
        DatabaseReference child = reference.child(curUser.getUid());

        // Put all the data to be input into the database.
        child.child("email").setValue(email.getText().toString());
        child.child("name").setValue(name.getText().toString());
    }

    public boolean checkInputs(){

        // inputOk is true until an invalid input is found.
        boolean inputOK = true;

        // If email is not entered or is not in the form of an email, the email input is invalid.
        if(TextUtils.isEmpty(email.getText())){
            email.setError("Email is required.");
            inputOK = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            email.setError("This email address is not a valid email.");
            inputOK = false;
        }

        // If the password or confirmation password is not entered, the password is not at least
        // 6 characters long, or the password and the confirmation password do not match,
        // the password and confirmation password is invalid.
        if(TextUtils.isEmpty(password.getText())){
            password.setError("Password is required.");
            inputOK = false;
        }
        else if(password.getText().toString().length() < 6){
            password.setError("The password must be at least 6 characters long");
            inputOK = false;
        }
        else if(TextUtils.isEmpty(confirmPassword.getText())){
            confirmPassword.setError("Confirm password is required.");
            inputOK = false;
        }
        else if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            password.setError("Password does not match confirmation password.");
            password.setText("");
            confirmPassword.setText("");
            inputOK = false;
        }
        // If the name is not entered, then the name is invalid.
        if(TextUtils.isEmpty(name.getText())){
            name.setError("Name is required.");
            inputOK = false;
        }

        return inputOK;
    }

    //Registers a user using Firebase Authentication
    public void RegisterUser() {
        boolean inputOK = checkInputs();
        if (inputOK) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),
                    password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                public void onComplete(Task<AuthResult> task) {
                    try {
                        //check if successful
                        if (task.isSuccessful()) {
                            //Adds other data to the database using current user authentication
                            //User is successfully registered and logged in
                            //start Profile Activity here
                            Toast.makeText(register_page.this, "Registration Successful",
                                    Toast.LENGTH_SHORT).show();
                            curUser = mAuth.getCurrentUser();
                            addUserToDatabase();
                            startActivity(new Intent(getApplicationContext(), caregiver_info.class));
                            finish();
                        } else {
                            Toast.makeText(register_page.this, "Couldn't register, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}