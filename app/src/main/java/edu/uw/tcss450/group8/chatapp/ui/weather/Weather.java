package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;

/**
 * Class to encapsulate weather forecasts.
 *
 * @author shilnara dam
 * @version 5/13/22
 */
public class Weather implements Serializable {
    private final String mCity;
    private final String mTime;
    private final String mTemp;
    private final String mCondition;
    private final String mIcon;

    /**
     * constructor for weather at specific city and time
     *
     * @param theCity String the city
     * @param theTime String the time
     * @param theTemp String the temperature
     * @param theCondition String the condition
     */
    public Weather(String theCity, String theTime, String theTemp, String theCondition, String theIcon) {
        mCity = theCity;
        mTime = theTime;
        mTemp = String.valueOf((int) Double.parseDouble(theTemp)) + "\u00B0";
        mCondition = theCondition;
        mIcon = "https://openweathermap.org/img/wn/" + theIcon + "@2x.png";
    }


    /**
     * getter for city
     *
     * @return the city
     */
    public String getCity() {
        return mCity;
    }

    /**
     * getter for time
     *
     * @return the time
     */
    public String getTime() {
        return mTime;
    }

    /**
     * getter for temperature
     *
     * @return the temperature
     */
    public String getTemp() {
        return mTemp;
    }

    /**
     * getter for condition
     *
     * @return the condition
     */
    public String getCondition() {
        return mCondition;
    }

    /**
     * getter for weather icon id
     *
     * @return the condition
     */
    public String getIcon() {
        return mIcon;
    }
}
