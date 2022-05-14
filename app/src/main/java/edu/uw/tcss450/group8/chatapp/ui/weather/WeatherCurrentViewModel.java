package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import edu.uw.tcss450.group8.chatapp.io.RequestQueueSingleton;

/**
 * View Model class for current weather
 *
 * @author shilnara dam
 * @version 2.0
 */
public class WeatherCurrentViewModel extends AndroidViewModel {
    private MutableLiveData<Weather> mWeather;

    /**
     * view model class for current weather.
     *
     * @param application application
     */
    public WeatherCurrentViewModel(@NonNull Application application) {
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
     * sends a JSON request for the current weather based on zipcode
     * the zipcode is sent through the url
     *
     * @param theZipcode int the zipcode of the desired location
     */
    public void getCurrentWeatherZipCode(int theZipcode) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/current/zipcde/" + theZipcode;

        //sending JSON request GET
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError); {

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * sends a JSON request for the current weather based on latitude and longitude
     * latitude and longitude is sent through the request body in JSON form
     *
     * @param theLat double latitude of desired location
     * @param thelong double longitude of desired location
     */
    public void getCurrentWeatherLatLong(double theLat, double thelong) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/current/lat-long";

        //setting up body of request
        //contains lat and long
        HashMap<String, Double> coord = new HashMap<String, Double>();
        coord.put("latitude", theLat);
        coord.put("longitude", thelong);
        JSONObject body = new JSONObject(coord);

        //sending JSON request GET
        Request request = new JsonObjectRequest(
            Request.Method.GET,
            url,
            body,
            this::handleResult,
            this::handleError); {

        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
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
        //if result doesnt contain a city, then it doesn't exist
        if (!theResult.has("city")) {

        }
        // try to set current weather
        try {
            //check if correct result string names
            mWeather.setValue(new Weather(
                    theResult.getString("city"),
                    theResult.getString("time"),
                    theResult.getString("temp"),
                    theResult.getString("condition")
            ));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage());
        }
    }

}
