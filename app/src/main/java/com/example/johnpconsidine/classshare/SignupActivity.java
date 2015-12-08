package com.example.johnpconsidine.classshare;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.johnpconsidine.classshare.ParseClasses.ParseConstants;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    public final static String TAG = SignupActivity.class.getSimpleName();
    protected EditText mFirstName;
    protected  EditText mLastName;
    protected EditText mEmail;
    protected EditText mPassword;
    protected EditText mConfirm;
    protected Button mSignupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //getEdit texts
        mEmail = (EditText) findViewById(R.id.emailText);
        mFirstName = (EditText) findViewById(R.id.firstnameText);
        mLastName = (EditText) findViewById(R.id.lastnameText);
        mPassword = (EditText) findViewById(R.id.passwordText);
        mConfirm = (EditText) findViewById(R.id.confirmText);

        //set up buttons
        mSignupButton = (Button) findViewById(R.id.signupButton);
        mSignupButton.setOnClickListener(click);

    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Send info to Parse
            Log.v(TAG, mEmail.getText().toString());
            //set trimmed strings
            final String firstname = mFirstName.getText().toString().trim();
            final String lastname = mLastName.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String confirm = mConfirm.getText().toString().trim();
            final String email = mEmail.getText().toString().trim();

            if (firstname.isEmpty() || lastname.isEmpty() || password.isEmpty() || email.isEmpty() || confirm.isEmpty()) {
                //maek sure all fields are filled
                showError(getString(R.string.empty_fields_error));
            }
            //passwords dont match
            else if (!password.equals(confirm)) {
                showError(getString(R.string.unmatch_error));
                Log.v(TAG, "password: " + password + " confirm: " + confirm);
                mConfirm.setText(""); // clear the text for convenience
            }
            //password to short
            else if (password.length() < 6) {
                showError(getString(R.string.short_error));
                mConfirm.setText("");
                mPassword.setText("");
            }
            //make account
            else {
                setProgressBarIndeterminateVisibility(true);
                ParseUser newUser = new ParseUser();
                newUser.setUsername(email);
                newUser.setPassword(password);
                newUser.put(ParseConstants.FIRSTNAME, firstname);
                newUser.put(ParseConstants.LASTNAME, lastname);
                   // and classes


                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {//no error
                            //initialize empty groups and classes

                            setProgressBarIndeterminateVisibility(false);
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {//error
                            showError(e.getMessage());
                        }
                    }
                });
            }


        }


    };
    private void showError(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setMessage(error);
        builder.setTitle(R.string.general_error_title);
        builder.setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
