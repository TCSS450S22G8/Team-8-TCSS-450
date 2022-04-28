package edu.uw.tcss450.group8.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;


import android.os.Bundle;

import com.auth0.android.jwt.JWT;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //test comment
        super.onCreate(savedInstanceState);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home)
                .build();



        setContentView(R.layout.activity_main);
    }
}
