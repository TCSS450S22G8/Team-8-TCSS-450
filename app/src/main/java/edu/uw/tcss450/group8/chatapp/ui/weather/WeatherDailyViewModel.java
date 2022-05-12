package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Class View Model for current weather
 *
 * @author shilnara dam
 * @version 2.0
 */
public class WeatherDailyViewModel extends AndroidViewModel {
    private MutableLiveData<Weather> mWeather;

    /**
     * view model class for daily weather.
     *
     * @param application application
     */
    public WeatherDailyViewModel(@NonNull Application application) {
        super(application);
        mWeather = new MutableLiveData<>();
    }

    /**
     * adds an observer to this live data for the variable mWeather.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addWeatherObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super Weather> observer) {
        mWeather.observe(owner, observer);
    }

    /**
     * sends a JSON request for the daily weather based on zipcode
     * gets daily weather
     *
     * @param theZipcode int the zipcode of the desired location
     */
    public void getCurrentWeatherZipCode(int theZipcode) {

    }

    /**
     * sends a JSON request for the daily weather based on latitude and longitude
     * gets daily weather
     *
     * @param theLat double latitude of desired location
     * @param thelong double longitude of desired location
     */
    public void getCurrentWeatherLatLong(double theLat, double thelong) {

    }

    /**
     * handler for request errors
     *
     * @param theError VolleyError request error
     */
    private void handleError(final VolleyError theError) {

    }

    /**
     * handler for successful requests
     *
     * @param theResult JSONObject of resulting request
     */
    private void handleResult(final JSONObject theResult) {

    }

}
