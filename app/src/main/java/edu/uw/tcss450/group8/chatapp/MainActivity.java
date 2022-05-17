package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.setEmail;
import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.setJWT;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;

import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.settings.SettingFragmentDirections;

/**
 * Class for Main Activity
 * Top level container for fragments after user signs in.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());
        //Import com.auth0.android.jwt.JWT
        JWT jwt = new JWT(args.getJwt());

        setJWT(this, args.getJwt());
        setEmail(this, args.getJwt());
        // Check to see if the web token is still valid or not. To make a JWT expire after a
        // longer or shorter time period, change the expiration time when the JWT is
        // created on the web service.
        // TODO: Signing in JWT is expired sometimes, send a request back to backend to do verification and send new JWT back

        try {
            if (!jwt.isExpired(0)) {
                new ViewModelProvider(
                        this,
                        new UserInfoViewModel.UserInfoViewModelFactory(jwt))
                        .get(UserInfoViewModel.class);
            } else {
                JwtExpire();
            }
        } catch (IllegalStateException e) {
            JwtExpire();
        }


        setCustomizedThemes(this, getThemeColor(this));
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment, R.id.nav_connections_fragment, R.id.nav_chat_fragment, R.id.nav_weather_fragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    //back button for home
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /**
     * helper method when jwt expire.
     */
    private void JwtExpire() {
        setJWT(this, "");
        setEmail(this, "");

        Navigation.findNavController(this, R.id.mainActivity).navigate(
                SettingFragmentDirections
                        .actionSettingFragmentToAuthenticationActivity());
        this.finish();
    }
}
