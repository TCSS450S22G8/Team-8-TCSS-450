package edu.uw.tcss450.group8.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class to manage login status
 *
 * @author JenHo Liao
 * @version 5/12/22
 */
public class LogInStatusManager {

    /**
     * save JWT to context's preferences.
     *
     * @param context the context.
     * @param jwt     the jwt
     */
    public static void setJWT(Context context, String jwt) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("jwt_string", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("jwt", jwt);
        editor.apply();
    }

    /**
     * get JWT from context's preferences.
     *
     * @param context the context.
     * @return the jwt
     */
    public static String getJWT(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("jwt_string", Context.MODE_PRIVATE);
        return sharedpreferences.getString("jwt", "");
    }

    /**
     * set email to context's preferences.
     *
     * @param context the context
     * @param email   the email
     */
    public static void setEmail(Context context, String email) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("email_string", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }

    /**
     * get email from context's preferences.
     *
     * @param context the context
     * @return the email
     */
    public static String getEmail(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("email_string", Context.MODE_PRIVATE);
        return sharedpreferences.getString("email", "");
    }
}
