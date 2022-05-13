package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;

//import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        setCustomizedThemes(this,getThemeColor(this));
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
}
