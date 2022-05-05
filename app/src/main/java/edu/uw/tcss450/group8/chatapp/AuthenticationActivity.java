package edu.uw.tcss450.group8.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Activity for login and registration.
 *
 * @author Sean Logan
 * @version 1.0
 */
public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}