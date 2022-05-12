package edu.uw.tcss450.group8.chatapp.ui.weather;

import java.io.Serializable;

/**
 * Class to encapsulate weather forecasts.
 *
 * @author shilnara dam
 * @version 2.0
 */
public class Weather implements Serializable {
    private final String mCity;
    private final String mTime;
    private final String mTemp;
    private final String mCondition;

    /**
     * constructor for weather at specific city and time
     *
     * @param theCity String the city
     * @param theTime String the time
     * @param theTemp String the temperature
     * @param theCondition String the condition
     */
    public Weather(String theCity, String theTime, String theTemp, String theCondition) {
        mCity = theCity;
        mTime = theTime;
        mTemp = theTemp;
        mCondition = theCondition;
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
}
