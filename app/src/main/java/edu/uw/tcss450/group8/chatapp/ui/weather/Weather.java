package edu.uw.tcss450.group8.chatapp.ui.weather;

import java.io.Serializable;


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
        mTemp = (int) Double.parseDouble(theTemp) + "\u00B0" + "F";
        mCondition = theCondition;
        mIcon = "https://openweathermap.org/img/wn/" + theIcon + "@2x.png";
    }

    /**
     * constructor for weather at specific city and time
     *
     * @param theCity String the city
     * @param theTime String the time
     * @param theDayTemp String the day temperature
     * @param theNightTemp String the night temperature
     * @param theCondition String the condition
     */
    public Weather(String theCity, String theTime, String theDayTemp, String theNightTemp, String theCondition, String theIcon) {
        mCity = theCity;
        mTime = theTime;
        String day = (int) Double.parseDouble(theDayTemp) + "\u00B0";
        String night = (int) Double.parseDouble(theNightTemp) + "\u00B0";
        mTemp = day + "/" + night + "F";
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
