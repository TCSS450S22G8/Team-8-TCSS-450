package edu.uw.tcss450.group8.chatapp.ui.location;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Location object
 *
 * @author shilnara dam
 * @version 5/27/22
 */
public class Location implements Serializable {
    private final String mCity;
    private String mZipCode;
    private String mLat;
    private String mLon;

    /**
     * constructor for location based on zipcode
     *
     * @param theCity String the city
     * @param theZipCode String the zipcode
     */
    public Location (String theCity, String theZipCode) {
        mCity = theCity;
        mZipCode = theZipCode;
    }

    /**
     * constructor for location based on latitude and longitude
     *
     * @param theCity String the city
     * @param theLat String the latitude
     * @param theLon String the longitude
     */
    public Location (String theCity, String theLat, String theLon) {
        mCity = theCity;
        mLat = theLat;
        mLon = theLon;
    }

    /**
     * tostring method for location
     * @return string representation of location
     */
    @NonNull
    @Override
    public String toString() {
        return "{City: " + mCity + ", Lat/Lon: " + mLat + "/" + mLon + "}";
    }

    /**
     * returns the city
     *
     * @return String the city
     */
    public String getCity() {
        return mCity;
    }

    /**
     * returns the zipcode
     *
     * @return String the zipcode
     */
    public String getZipCode() {
        return mZipCode;
    }

    /**
     * returns the latitude
     *
     * @return String the latitude
     */
    public String getLat() {
        return mLat;
    }

    /**
     * returns the longitude
     *
     * @return String the longitude
     */
    public String getLon() {
        return mLon;
    }
}
