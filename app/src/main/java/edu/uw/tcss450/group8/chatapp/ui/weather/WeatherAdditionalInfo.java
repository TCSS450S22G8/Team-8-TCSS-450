package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.annotation.SuppressLint;

import java.io.Serializable;

/**
 * class to encapsulate additional current weather information
 *
 * @author shilnara dam
 * @version 6/4/22
 */
public class WeatherAdditionalInfo implements Serializable {

    private final String mWind;
    private final String mHumidity;
    private final String mDewPoint;
    private final String mPressure;
    private final String mUVIndex;
    private final String mCloudiness;
    private final String mSunrise;
    private final String mSunset;

    /**
     * constructor for WeatherAdditionalInfo Object
     * @param theWind String the wind
     * @param theDirection String the wind direction
     * @param theHumidity String the humidity
     * @param theDewPoint String the dew point
     * @param thePressure String the pressure
     * @param theUVIndex String the uv index
     * @param theCloudiness String the percentage of clouds
     * @param theSunrise String the sunrise
     * @param theSunset String the sunset
     */
    public WeatherAdditionalInfo(String theWind, String theDirection, String theHumidity,
                                 String theDewPoint, String thePressure, String theUVIndex,
                                 String theCloudiness, String theSunrise, String theSunset) {
        this.mWind = toTextualDescription(Double.parseDouble(theDirection),
                Double.parseDouble(theWind));
        this.mHumidity = theHumidity + "%";
        this.mDewPoint = theDewPoint + "\u00B0";
        this.mPressure = setPressure(Double.parseDouble(thePressure));
        this.mUVIndex = setUVIndex(Integer.parseInt(theUVIndex));
        this.mCloudiness = theCloudiness + "%";
        this.mSunrise = theSunrise;
        this.mSunset = theSunset;
    }

    /**
     * adds the LOW/MODERATE/HIGH to the uvi index
     *
     * @param uv int uv index
     * @return uv index with low/moderate/high
     */
    private String setUVIndex(int uv) {
        String uvIndex = "";
        if (uv <= 2) {
            uvIndex = uv + " LOW";
        } else if (uv <= 7) {
            uvIndex = uv + " MODERATE";
        } else {
            uvIndex = uv + " HIGH";
        }
        return uvIndex;
    }

    /**
     * converts hPa to IN
     *
     * @param hpa double pressure in hPa
     * @return pressure in IN
     */
    @SuppressLint("DefaultLocale")
    private String setPressure(double hpa) {
        return String.format("%.2f", (hpa * .02953)) + " IN";
    }



    /**
     * converts the wind direction in degrees to cardinal directions
     *
     * @param degree String thee wind direction in degrees.
     * @param wind double speed of wind in MPH
     * @return String representation of wind speed and direction
     */
    public String toTextualDescription(double degree, double wind) {
        String[] directions = {"N","NNE","NE","ENE","E","ESE", "SE", "SSE","S",
                "SSW","SW","WSW","W","WNW","NW","NNW"};
        //divide by 22.5 since that is 360/16, add .5 to break ties, cast to int,
        //then mod 16 to find which sector it is in
        int index = ((int) ((degree/22.5)+.5)) % 16;
        return directions[index] +  " " + (int) wind + " MPH";
    }

    /**
     * gets wind
     * @return the wind
     */
    public String getWind() {
        return mWind;
    }

    /**
     * gets humidity
     * @return the humidity
     */
    public String getHumidity() {
        return mHumidity;
    }

    /**
     * gets dew point
     * @return the dew point
     */
    public String getDewPoint() {
        return mDewPoint;
    }

    /**
     * gets pressure
     * @return the pressure
     */
    public String getPressure() {
        return mPressure;
    }

    /**
     * gets uv index
     * @return the uv index
     */
    public String getUVIndex() {
        return mUVIndex;
    }

    /**
     * gets the percentage of clouds
     * @return the percentage of clouds
     */
    public String getCloudiness() {
        return mCloudiness;
    }

    /**
     * gets sunrise
     * @return the sunrise
     */
    public String getSunrise() {
        return mSunrise;
    }

    /**
     * gets sunset
     * @return the sunset
     */
    public String getSunset() {
        return mSunset;
    }
}


