package com.birdicomputers.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText email_text;
    EditText pass_text;
    final String TAG = "Come Here";
    Button signup;
    String email;
    String password;
    boolean connected = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        signup = findViewById(R.id.signUpButton);
        email_text = findViewById(R.id.signUpEmail);
        pass_text = findViewById(R.id.signUpPass);
        AccentColors();
        signup.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(checkConnection()){
                Signup();
            }
            else{
                Toast.makeText(SignupActivity.this, "Please connect to the Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Signup(){
        email = email_text.getText().toString();
        password = pass_text.getText().toString();
        if (email.length() > 0 && password.length() > 0){
            if(password.length()>=9){
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                Toast.makeText(SignupActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                // If sign in fails, display a message to the user.
                                // Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "There's an exiting account with same Email.",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Toast.makeText(SignupActivity.this, "Password should at least have 9 characters.", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(SignupActivity.this, "Oh no, seems like you missed a spot!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }

    public void AccentColors(){
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorTheme)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorTheme));
        }
    }
}

