package com.example.karimabounassif.capstone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final myDbAdapter helper = new myDbAdapter(this);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);                           // Set up the login form.
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {       //Run login if enter key pressed
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (attemptLogin()) {
                        String[] user = helper.checkUserPW(mEmailView.getText().toString(), mPasswordView.getText().toString());
                        Bundle b = new Bundle();
                        b.putStringArray("user", user);
                        Intent login = new Intent(getBaseContext(), EntryListActivity.class);
                        login.putExtras(b);
                        startActivity(login);
                    }

                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);           //Same code but for manually clicking
        mEmailSignInButton.setOnClickListener(new OnClickListener() {                           //login button
            @Override
            public void onClick(View view) {
                if (attemptLogin()) {
                    String[] user = helper.checkUserPW(mEmailView.getText().toString(), mPasswordView.getText().toString());
                    Bundle b = new Bundle();
                    b.putStringArray("user", user);
                    Intent login = new Intent(getBaseContext(), EntryListActivity.class);
                    login.putExtras(b);
                    startActivity(login);
                }
            }
        });

        Button mStartup = (Button) findViewById(R.id.startup_signin);                       //Startup button sends user to
        mStartup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {                                              //appropriate screen.
                if (attemptLogin()) {
                    Intent login = new Intent(getBaseContext(), StartupFill.class);
                    startActivity(login);
                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private Boolean attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        myDbAdapter helper = new myDbAdapter(this);

        if (isEmailValid(email) && isPasswordValid(password)) {
            String[] user = helper.checkUserPW(email, password);
            if (user[0].equals("Wrong password.") || user[0].equals("No such username.")) {
                mEmailView.setText(user[0]);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 4;
    }
}