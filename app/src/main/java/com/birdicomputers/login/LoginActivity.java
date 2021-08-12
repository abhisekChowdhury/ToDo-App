package com.birdicomputers.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.HapticFeedbackConstants;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    Button login;
    TextView signup, resetPassword;
    EditText email_text, pass_text;
    String email, password;
    boolean connected = false;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AccentColors();
        auth = FirebaseAuth.getInstance();
        login = findViewById(R.id.loginButton);
        signup = findViewById(R.id.signupButtonText);
        email_text = findViewById(R.id.loginEmail);
        pass_text = findViewById(R.id.loginPassword);
        resetPassword = findViewById(R.id.forgotPassButtonText);

        resetPassword.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent forgotPasswordActivty = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(forgotPasswordActivty);
        });
        signup.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(v -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            if(checkConnection()){
                Login();
            }
            else {
                Toast.makeText(LoginActivity.this, "Please connect to the Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Login(){
        email = email_text.getText().toString();
        password = pass_text.getText().toString();
        if(email.length() > 0 && password.length() > 0){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successfully Logged In",Toast.LENGTH_SHORT).show();
                    Intent main_acitvity = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(main_acitvity);
                }
                else{
                    Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(LoginActivity.this,"Please enter email and password!",Toast.LENGTH_SHORT).show();
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
