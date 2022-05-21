package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import edu.uw.tcss450.group8.chatapp.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

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

        //If it is not already running, start the Pushy listening service
        //Always keep this at the end of this method
        Pushy.listen(this);

        initiatePushyTokenRequest();
    }

    /**
     * Helper method to request PushyViewModel to get the pushy token
     */
    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }
}