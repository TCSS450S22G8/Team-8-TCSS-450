package edu.uw.tcss450.group8.chatapp;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setCustomizedThemes;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auth0.android.jwt.JWT;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.tcss450.group8.chatapp.databinding.ActivityMainBinding;
import edu.uw.tcss450.group8.chatapp.model.NewFriendRequestCountViewModel;
import edu.uw.tcss450.group8.chatapp.model.NewMessageCountViewModel;
import edu.uw.tcss450.group8.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.services.PushReceiver;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.Message;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.MessageListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationViewModel;

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

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    // A constant int for the permissions request code. Must be a 16 bit number
    private static final int MY_PERMISSIONS_LOCATIONS = 8414;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding mBinding;
    private MainPushMessageReceiver mPushMessageReceiver;
    private NewMessageCountViewModel mNewMessageModel;
    private NewFriendRequestCountViewModel mNewFriendRequestModel;
    private LocationRequest mLocationRequest;
    //Use a FusedLocationProviderClient to request the location
    private FusedLocationProviderClient mFusedLocationClient;
    // Will use this call back to decide what to do when a location change is detected
    private LocationCallback mLocationCallback;
    //The ViewModel that will store the current location
    private LocationViewModel mLocationModel;
    private ChatroomViewModel mChatroomViewModel;
    private UserInfoViewModel mUserModel;
    private LocationListViewModel mLocationListModel;
    private ContactListViewModel mContactModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        onNewIntent(getIntent());
        super.onCreate(savedInstanceState);

        MainActivityArgs args = MainActivityArgs.fromBundle(getIntent().getExtras());

        //Import com.auth0.android.jwt.JWT
        JWT jwt = new JWT(args.getJwt());

        // Check to see if the web token is still valid or not. To make a JWT expire after a
        // longer or shorter time period, change the expiration time when the JWT is
        // created on the web service.
        // TODO: Signing in JWT is expired sometimes, send a request back to backend to do verification and send new JWT back

        try {
            if (!jwt.isExpired(5)) {
                Log.e("Tag", "inside if");
                new ViewModelProvider(
                        this,
                        new UserInfoViewModel.UserInfoViewModelFactory(args.getEmail(), args.getJwt()))
                        .get(UserInfoViewModel.class);

            } else {
                signOut();
            }
        } catch (IllegalStateException e) {
            signOut();
        }


        // load contact and chatroom as soon as login

        ViewModelProvider viewModelProvider = new ViewModelProvider(this);

        mUserModel = viewModelProvider.get(UserInfoViewModel.class);
        mChatroomViewModel = viewModelProvider.get(ChatroomViewModel.class);
        mContactModel = viewModelProvider.get(ContactListViewModel.class);
        mLocationModel = viewModelProvider.get(LocationViewModel.class);
        mLocationListModel = viewModelProvider.get(LocationListViewModel.class);
        mContactModel.getContacts(mUserModel.getJwt());
        mChatroomViewModel.getChatRoomsForUser(mUserModel.getJwt());

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mLocationListModel.getLocations(mUserModel.getJwt());
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
        mNewFriendRequestModel = new ViewModelProvider(this).get(NewFriendRequestCountViewModel.class);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.messageListFragment) {
                //When the user navigates to the chats page, reset the new message count.
                //This will need some extra logic for your project as it should have
                //multiple chat rooms.
                mNewMessageModel.update();
            }
            // Resets friend request badge count to 0 if we are on the connections fragment
            else if (destination.getId() == R.id.nav_connections_fragment) {
                mNewFriendRequestModel.reset();
            }
        });

        mNewMessageModel.addNewMessageCountObserver(this, count -> {
            BadgeDrawable badge = mBinding.navView.getOrCreateBadge(R.id.nav_chatroom_fragment);
            badge.setBackgroundColor(getResources().getColor(R.color.badge_red, null));
//            badge.setMaxCharacterCount(2);
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

        // Update badge for connections nav element
        mNewFriendRequestModel.addFriendRequestCountObserver(this, count -> {
            BadgeDrawable badge = mBinding.navView.getOrCreateBadge(R.id.nav_connections_fragment);
//            badge.setMaxCharacterCount(2);

            mContactModel.getOutgoingRequestList(mUserModel.getJwt());
            mContactModel.getIncomingRequestList(mUserModel.getJwt());
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

        //request user location permission and gets location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                            , Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_LOCATIONS);
        } else {
            //The user has already allowed the use of Locations. Get the current location.
            requestLocation();
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
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("menuFragment")) {
                // todo move fragment
            }
        }
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
        if (mPushMessageReceiver != null) {
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
                break;
            case R.id.action_sign_out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();

        PushyTokenViewModel model = new ViewModelProvider(this)
                .get(PushyTokenViewModel.class);
        //when we hear back from the web service quit
        model.addResponseObserver(this, result -> finishAndRemoveTask());
        model.deleteTokenFromWebservice(
                new ViewModelProvider(this)
                        .get(UserInfoViewModel.class)
                        .getJwt()
        );

        //Go back to login page
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.authenticationActivity);

        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_LOCATIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // locations-related task you need to do.
                    requestLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("PERMISSION DENIED", "Nothing to see or do here.");

                    //Shut down the app. In production release, you would let the user
                    //know why the app is shutting down...maybe ask for permission again?
                    finishAndRemoveTask();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**
     * request of user location and saving it to location view model
     */
    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("LOCATION", "User did NOT allow permission to request location!");
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.e("LOCATION", location.toString());
                                if (mLocationModel == null) {
                                    mLocationModel = new ViewModelProvider(MainActivity.this)
                                            .get(LocationViewModel.class);
                                }
                                mLocationModel.setLocation(location);
                            }
                        }
                    });
        }
    }

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
                mNewMessageModel.increment(intent.getIntExtra("chatid", 0));

                //Inform the view model holding chatroom messages of the new
                //message.
                mModel.addMessage(intent.getIntExtra("chatid", -1), cm);

                MediaPlayer mediaplayer = MediaPlayer.create(MainActivity.this, R.raw.slap);
                mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    }
                });
                mediaplayer.start();
                return;
            }

            // Increment count for messages (for new chatroom)
            if (intent.hasExtra("addedToChat")) {
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                mNewMessageModel.increment(Integer.valueOf(intent.getStringExtra("chatid")));
                mChatroomViewModel.getChatRoomsForUser(mUserModel.getJwt());
                final ImageView ToastImageAdd = new ImageView(MainActivity.this);
                ToastImageAdd.setImageResource(R.drawable.slapchaticon);
                Toast.makeText(MainActivity.this, intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                return;
            }


            //Incrementing count for new friend requests
            if (intent.hasExtra("friendRequest")) {
                mContactModel.getIncomingRequestList(mUserModel.getJwt());
                mContactModel.getContacts(mUserModel.getJwt());
                final ImageView ToastImageAdd = new ImageView(MainActivity.this);
                ToastImageAdd.setImageResource(R.drawable.slapchaticon);
                Toast.makeText(MainActivity.this, intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                if (nd.getId() != R.id.nav_connections_fragment) {
                    mNewFriendRequestModel.increment();
                }
                return;
            }

            if (intent.hasExtra("deleteFriend")) {
                mContactModel.getContacts(mUserModel.getJwt());
                final ImageView ToastImageAdd = new ImageView(MainActivity.this);
                ToastImageAdd.setImageResource(R.drawable.slapchaticon);
                Toast.makeText(MainActivity.this, intent.getStringExtra("message"), Toast.LENGTH_SHORT).show();
                if (nd.getId() != R.id.nav_connections_fragment) {
                    mNewFriendRequestModel.increment();
                }
                return;
            }

            if (intent.hasExtra("deleteAccount")) {
                Intent i = new Intent (MainActivity.this, AuthenticationActivity.class);
                startActivity(i);
                signOut();
            }

            if (intent.hasExtra("deletedFromChat")) {
                mChatroomViewModel.getChatRoomsForUser(mUserModel.getJwt());
                return;
            }
        }
    }
}