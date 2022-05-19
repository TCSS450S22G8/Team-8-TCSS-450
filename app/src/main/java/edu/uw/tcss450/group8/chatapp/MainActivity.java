package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.setEmail;
import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.setJWT;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.auth0.android.jwt.JWT;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.group8.chatapp.databinding.ActivityMainBinding;
import edu.uw.tcss450.group8.chatapp.model.NewMessageCountViewModel;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.services.PushReceiver;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.Message;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.MessageListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.settings.SettingFragmentDirections;

/**
 * Class for Main Activity
 * Top level container for fragments after user signs in.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @author JenHo Liao
 * @author Rih Pham
 * @version 5/19/22
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private ActivityMainBinding mBinding;
    private MainPushMessageReceiver mPushMessageReceiver;

    private NewMessageCountViewModel mNewMessageModel;

    /**
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class MainPushMessageReceiver extends BroadcastReceiver {

        private MessageListViewModel mModel =
                new ViewModelProvider(MainActivity.this)
                        .get(MessageListViewModel.class);

        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            MainActivity.this, R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            if (intent.hasExtra("chatMessage")) {

                Message cm = (Message) intent.getSerializableExtra("chatMessage");

                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                if (nd.getId() != R.id.messageListFragment) {
                    mNewMessageModel.increment();
                }
                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        new ViewModelProvider(this,
                new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt())
        ).get(UserInfoViewModel.class);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment, R.id.nav_connections_fragment, R.id.nav_chatroom_fragment, R.id.nav_weather_fragment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        mNewMessageModel = new ViewModelProvider(this).get(NewMessageCountViewModel.class);


        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.messageListFragment) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.reset();
            }
        });

        mNewMessageModel.addMessageCountObserver(this, count -> {
            BadgeDrawable badge = mBinding.navView.getOrCreateBadge(R.id.messageListFragment);
            badge.setMaxCharacterCount(2);
            if (count > 0) {
                //new messages! update and show the notification badge.
                badge.setNumber(count);
                badge.setVisible(true);
            } else {
                //user did some action to clear the new messages, remove the badge
                badge.clearNumber();
                badge.setVisible(false);
            }
        });
        //Import com.auth0.android.jwt.JWT
        JWT jwt = new JWT(args.getJwt());

        setJWT(this, args.getJwt());
        setEmail(this, args.getEmail());
        // Check to see if the web token is still valid or not. To make a JWT expire after a
        // longer or shorter time period, change the expiration time when the JWT is
        // created on the web service.
        // TODO: Signing in JWT is expired sometimes, send a request back to backend to do verification and send new JWT back

        try {
            if (!jwt.isExpired(5)) {
                new ViewModelProvider(
                        this,
                        new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt()))
                        .get(UserInfoViewModel.class);
            } else {
                JwtExpire();
            }
        } catch (IllegalStateException e) {
            JwtExpire();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        setCustomizedThemes(this, getThemeColor(this));
        return super.onCreateView(name, context, attrs);
    }

    //back button for home
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new MainPushMessageReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        registerReceiver(mPushMessageReceiver, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null){
            unregisterReceiver(mPushMessageReceiver);
        }
    }


    //onCreateOptionsMenu and onOptionsItemSelected are top right, 3 dot settings
    //toolbar.xml file
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //gets id for item selected, then sends user to corresponding fragment.
        int id = item.getItemId();
        switch (id) {
            case R.id.settingFragment:
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_setting_fragment);
        }
        return super.onOptionsItemSelected(item);
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
