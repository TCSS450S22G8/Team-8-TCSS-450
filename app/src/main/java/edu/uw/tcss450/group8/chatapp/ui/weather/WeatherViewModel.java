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
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.uw.tcss450.group8.chatapp.io.RequestQueueSingleton;

/**
 * View Model class for weather
 *
 * @author shilnara dam
 * @version 2.0
 */
public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<Weather> mCurrentWeather;
    private MutableLiveData<ArrayList<Weather>> mHourlyWeather;
    private MutableLiveData<ArrayList<Weather>> mDailyWeather;
    private JSONObject mResponse;

    /**
     * view model class for current weather.
     *
     * @param application application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mCurrentWeather = new MutableLiveData<>();
        mHourlyWeather = new MutableLiveData<>();
        mDailyWeather = new MutableLiveData<>();
    }

    /**
     * adds an observer to this live data for the variable mWeather.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addListWeatherObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Weather>> observer) {
        mHourlyWeather.observe(owner, observer);
        mDailyWeather.observe(owner, observer);
    }

    /**
     * adds an observer to this live data for the variable mWeather.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addCurrentWeatherObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super Weather> observer) {
        mCurrentWeather.observe(owner, observer);

    }

    /**
     * sends a JSON request for the current weather based on zipcode
     * the zipcode is sent through the url
     *
     * @param theZipcode String the zipcode of the desired location
     */
    public void getWeatherZipCode(final String theZipcode) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/zipcode/" + theZipcode;
        //String url = "http://10.0.2.2:5000/weather/zipcode";
        //create request
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * sends a JSON request for the current weather based on latitude and longitude
     * latitude and longitude is sent through url
     *
     * @param theLat String latitude of desired location
     * @param thelon String longitude of desired location
     */
    public void getWeatherLatLon(String theLat, String thelon) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/lat-lon/" + theLat + "/" + thelon;
        //creating request GET
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
     * handler for request errors
     *
     * @param theError VolleyError request error
     */
    private void handleError(final VolleyError theError) {
        if (Objects.isNull(theError.networkResponse)) {
            Log.e("NETWORK ERROR", theError.getMessage());
        }
        else {
            String data = new String(theError.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    theError.networkResponse.statusCode +
                            " " +
                            data);
        }

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
        mResponse = theResult;
        setCurrent();
        //setHourly();
        //setDaily();
    }

    /**
     * sets current weather
     *
     */
    private void setCurrent() {
        try {
            JSONObject current = mResponse.getJSONObject("current");
            JSONObject currentWeather = current.getJSONArray("weather").getJSONObject(0);
            Log.e("bit", currentWeather.getString("icon"));

            mCurrentWeather.setValue(
                    new Weather(
                            mResponse.getString("city"),
                            current.getString("dt"),
                            current.getString("temp"),
                            currentWeather.getString("description"),
                            currentWeather.getString("icon")
                    ));

        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success ForecastCurrentViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * sets hourly weather
     *
     */
    private void setHourly() {

    }

    /**
     * sets daily weather
     *
     */
    private void setDaily() {

    }

}
