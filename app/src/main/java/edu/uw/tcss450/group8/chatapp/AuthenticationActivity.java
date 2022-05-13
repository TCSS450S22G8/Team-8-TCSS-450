package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

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
        setCustomizedThemes(this,getThemeColor(this));
        setContentView(R.layout.activity_auth);
    }
}