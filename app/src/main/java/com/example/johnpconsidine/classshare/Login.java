package com.example.johnpconsidine.classshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login extends AppCompatActivity {
    public static final String TAG = Login.class.getSimpleName();
    protected EditText mUsername;
    protected  EditText mPassword;
    protected Button mLoginButton;
    protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(getWindow().FEATURE_INDETERMINATE_PROGRESS); // ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //For signin textview
        mSignUpTextView = (TextView)findViewById(R.id.signupText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        //set textviews etc
        mUsername = (EditText) findViewById(R.id.usernameText);
        mPassword = (EditText) findViewById(R.id.passwordText);
        mLoginButton = (Button) findViewById(R.id.loginButton);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login
                final String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString();


                password = password.trim();

                //todo cleean this up
                if (username.isEmpty() || password.isEmpty()) {
                    //error
                }
                else {
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if (e==null) {
                                //succeess
                                //set current user

                                // go to homepage
                                Intent intent = new Intent (Login.this, MainActivity.class);
                                intent.putExtra("CurrentUser", username);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(Login.this, "Error Logging in: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
