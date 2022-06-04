package edu.uw.tcss450.group8.chatapp.services;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.json.JSONException;

import edu.uw.tcss450.group8.chatapp.MainActivity;
import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.Message;
import me.pushy.sdk.Pushy;

/**
 * Class to notify users of new massages.
 *
 * @author Charles Bryan
 */
public class PushReceiver extends BroadcastReceiver {

    public static final String RECEIVED_NEW_MESSAGE = "new message from pushy";

    private static final String MESSAGE_CHANNEL_ID = "1";
    private static final String FRIEND_REQUEST_CHANNEL_ID = "2";
    private static final String DELETE_FRIEND_CHANNEL_ID = "3";
    private static final String ADD_FRIEND_TO_CHAT_CHANNEL_ID = "4";
    private static final String ACCEPT_FRIEND_REQUEST_CHANNEL_ID = "5";
    private static final String DELETED_FROM_CHAT_CHANNEL_ID = "6";


    @Override
    public void onReceive(Context context, Intent intent) {

        //the following variables are used to store the information sent from Pushy
        //In the WS, you define what gets sent. You can change it there to suit your needs
        //Then here on the Android side, decide what to do with the message you got

        //for the lab, the WS is only sending chat messages so the type will always be msg
        //for your project, the WS needs to send different types of push messages.
        //So perform logic/routing based on the "type"
        //feel free to change the key or type of values.
        String notificationType = intent.getStringExtra("type");
        switch (notificationType) {
            case "msg":
                messagePushNotification(context, intent);
                break;
            case "friendRequest":
                friendRequestNotification(context, intent);
                break;
            case "deleteFriend":
                deleteFriendNotification(context, intent);
                break;
            case "addedUserToChat":
                addFriendToChatNotification(context, intent);
                break;
            case "acceptFriendRequest":
                acceptFriendRequestNotification(context, intent);
                break;
            case "deleteUserFromChat":
                removeFromChatNotification(context, intent);
                break;
        }
    }

    /**
     * Push Notification when your friend accepts your friend request.
     *
     * @param context
     * @param intent
     */
    private void acceptFriendRequestNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            Log.d("PUSHY", "Message received in foreground: " + message);

            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("message", message);

            i.putExtra("friendRequest", "request");
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);
        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ACCEPT_FRIEND_REQUEST_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slapchaticon) //TODO: figure out why color isnt showing up
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a FriendRequestNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(5, builder.build());
        }
    }


    /**
     * Push Notification when you get added to a chat.
     *
     * @param context
     * @param intent
     */
    private void addFriendToChatNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);


        String chatId = intent.getStringExtra("chatid");
        Log.d("TAG", "addFriendToChatNotification: " + chatId);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            Log.d("PUSHY", "Message received in foreground: " + message);

            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("addedToChat", message);
            i.putExtra("chatid", chatId);
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);
        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, ADD_FRIEND_TO_CHAT_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slapchaticon) //TODO: figure out why color isnt showing up
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a FriendRequestNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(4, builder.build());
        }
    }


    /**
     * Push Notification when you get deleted as a friend.
     *
     * @param context
     * @param intent
     */
    private void deleteFriendNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            Log.d("PUSHY", "Message received in foreground: " + message);

            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("message", message);
            i.putExtra("deleteFriend", "request");
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);
        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DELETE_FRIEND_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slapchaticon) //TODO: figure out why color isnt showing up
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a FriendRequestNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(3, builder.build());
        }
    }


    /**
     * Push Notifications when friend request is received.
     *
     * @param context
     * @param intent
     */
    private void friendRequestNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            Log.d("PUSHY", "Message received in foreground: " + message);

            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("message", message);
            i.putExtra("friendRequest", "request");
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);
        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, FRIEND_REQUEST_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slapchaticon) //TODO: figure out why color isnt showing up
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a FriendRequestNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(2, builder.build());
        }
    }


    /**
     * Push Notification sent to user when a new message is received.
     *
     * @param context
     * @param intent
     */
    private void messagePushNotification(Context context, Intent intent) {
        Message message = null;
        int chatId = -1;
        try {
            message = Message.createFromJsonString(intent.getStringExtra("message"));
            chatId = intent.getIntExtra("chatid", -1);
            Log.d("TAG", "messagePushNotification: " + chatId);
        } catch (JSONException e) {
            //Web service sent us something unexpected...I can't deal with this.
            throw new IllegalStateException("Error from Web Service. Contact Dev Support");
        }

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            //app is in the foreground so send the message to the active Activities
            Log.d("PUSHY", "Message received in foreground: " + message);

            //create an Intent to broadcast a message to other parts of the app.
            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("chatMessage", message);
            i.putExtra("chatid", chatId);
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);

        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message.getMessage());

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());
            i.putExtra("chatid", chatId);

            context.sendBroadcast(i);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_chat_notification)
                    .setContentTitle("Message from: " + message.getUsername())
                    .setContentText(message.getMessage())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a ChatMessageNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(1, builder.build());
        }
    }


    /**
     * Push Notification sent to user when they have been removed from a chatroom.
     *
     * @param context
     * @param intent
     */
    private void removeFromChatNotification(Context context, Intent intent) {
        String message = intent.getStringExtra("message");

        ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(appProcessInfo);

        if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE) {
            Log.d("PUSHY", "Message received in foreground: " + message);

            Intent i = new Intent(RECEIVED_NEW_MESSAGE);
            i.putExtra("message", message);
            i.putExtra("deletedFromChat", "request");
            i.putExtras(intent.getExtras());

            context.sendBroadcast(i);
        } else {
            //app is in the background so create and post a notification
            Log.d("PUSHY", "Message received in background: " + message);

            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(intent.getExtras());

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    i, PendingIntent.FLAG_MUTABLE);

            //research more on notifications the how to display them
            //https://developer.android.com/guide/topics/ui/notifiers/notifications
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, DELETED_FROM_CHAT_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.slapchaticon) //TODO: figure out why color isnt showing up
                    .setContentTitle(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent);

            // Automatically configure a FriendRequestNotification Channel for devices running Android O+
            Pushy.setNotificationChannel(builder, context);

            // Get an instance of the NotificationManager service
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

            // Build the notification and display it
            notificationManager.notify(6, builder.build());
        }
    }
}
