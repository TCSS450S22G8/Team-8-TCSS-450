package edu.uw.tcss450.group8.chatapp.utils;

import android.content.Context;
import android.content.res.Configuration;

import android.app.AlertDialog;

import edu.uw.tcss450.group8.chatapp.R;

public class AlertBoxMaker {

    public static AlertDialog.Builder DialogWithStyle(Context context) {
        if (isDarkMode(context)) {
            return new AlertDialog.Builder(context, R.style.MyDialogThemeDark);
        }
        return new AlertDialog.Builder(context, R.style.MyDialogTheme);
    }

    private static boolean isDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }
}
