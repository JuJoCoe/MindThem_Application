package c.mindthem.mindthemapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by jorda on 4/11/2019.
 */

public class change_password extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser curUser;
    private EditText newPassword, confirmPassword;
    private Button button_change_password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepassword);

        newPassword =(EditText) findViewById(R.id.password_field);
        confirmPassword = (EditText) findViewById(R.id.confirm_password_field);

        button_change_password = (Button)findViewById(R.id.change_password_button);
        mAuth = FirebaseAuth.getInstance();

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button_change_password){
                    ChangePassword();
                }
            }
        });

    }

    public void ChangePassword() {
        boolean inputOK = checkInputs();
        if (inputOK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.updatePassword(newPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(change_password.this, "Password Changed",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), logs_alert_page.class));
                                finish();
                            }
                        }
                    });
        }




    }

    public boolean checkInputs(){
        // inputOk is true until an invalid input is found.
        boolean inputOK = true;

        // If the password or confirmation password is not entered, the password is not at least
        // 6 characters long, or the password and the confirmation password do not match,
        // the password and confirmation password is invalid.
        if(TextUtils.isEmpty(newPassword.getText())){
            newPassword.setError("Password is required.");
            inputOK = false;
        }
        else if(newPassword.getText().toString().length() < 6){
            newPassword.setError("The password must be at least 6 characters long");
            inputOK = false;
        }
        else if(TextUtils.isEmpty(confirmPassword.getText())){
            confirmPassword.setError("Confirm password is required.");
            inputOK = false;
        }
        else if(!newPassword.getText().toString().equals(confirmPassword.getText().toString())){
            newPassword.setError("Password does not match confirmation password.");
            newPassword.setText("");
            confirmPassword.setText("");
            inputOK = false;
        }


        return inputOK;
    }
}

