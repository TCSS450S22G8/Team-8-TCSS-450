package edu.uw.tcss450.group8.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import edu.uw.tcss450.group8.chatapp.R;

/**
 * Utility class to manage theme
 *
 * @author JenHo Liao
 * @version 5/12/22
 */
public class ThemeManager {

    /**
     * set theme for selected context
     *
     * @param context the context to themed
     * @param theme   the theme
     */
    public static void setCustomizedThemes(Context context, String theme) {
        switch (theme) {
            case "orange":
                context.setTheme(R.style.Theme_MyApplication);
                break;
            case "red":
                context.setTheme(R.style.Theme_Red);
                break;
            case "blue":
                context.setTheme(R.style.Theme_Blue);
                break;
            case "green":
                context.setTheme(R.style.Theme_Green);
                break;

        }
    }

    /**
     * save theme color to preferences
     *
     * @param context    preferences of context
     * @param themeColor theme color
     */
    public static void setThemeColor(Context context, String themeColor) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("theme", themeColor);
        editor.apply();
    }

    /**
     * get theme color from preferences
     *
     * @param context preferences of context
     * @return theme color
     */
    public static String getThemeColor(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("theme", "orange");
    }
}
