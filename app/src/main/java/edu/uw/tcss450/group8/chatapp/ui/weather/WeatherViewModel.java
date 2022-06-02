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


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import edu.uw.tcss450.group8.chatapp.io.RequestQueueSingleton;

/**
 * View Model class for weather
 *
 * @author shilnara dam
 * @version 5/15/22
 */
public class WeatherViewModel extends AndroidViewModel {

    private final MutableLiveData<Weather> mCurrentWeather;
    private final MutableLiveData<ArrayList<Weather>> mHourlyWeather;
    private final MutableLiveData<ArrayList<Weather>> mDailyWeather;
    private MutableLiveData<String> mError;
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
        mError = new MutableLiveData<>();
    }

    public void resetError() {
        mError = new MutableLiveData<>();
    }

    /**
     * adds an observer to this live data for the variable mHourlyWeather.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addHourlyWeatherObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<Weather>> observer) {
        mHourlyWeather.observe(owner, observer);
    }

    /**
     * adds an observer to this live data for the variable mDailyWeather.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addDailyWeatherObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super List<Weather>> observer) {
        mDailyWeather.observe(owner, observer);
    }


    /**
     * adds an observer to this live data for the variable mError.
     * Value gets updated if error from api call has some failure.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addErrorObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super String> observer) {
        mError.observe(owner, observer);
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
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleErrorZipcode);
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
     * @param theLon String longitude of desired location
     */
    public void getWeatherLatLon(String theLat, String theLon) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/lat-lon/" + theLat + "/" + theLon;
        //creating request GET
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleResult,
                this::handleErrorLatLon); {

        }
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * handler for zipcode request errors
     *
     * @param theError VolleyError request error
     */
    private void handleErrorZipcode(final VolleyError theError) {
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
        mError.setValue("zipcode");
    }

    /**
     * handler for lat/lon request errors
     *
     * @param theError VolleyError request error
     */
    private void handleErrorLatLon(final VolleyError theError) {
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
        mError.setValue("lat/lon");
    }



    /**
     * handler for successful requests
     *
     * @param theResult JSONObject of resulting request
     */
    private void handleResult(final JSONObject theResult) {
        //if result doesn't contain a city, then it doesn't exist
        if (!theResult.has("city")) {
            Log.e("WEATHER", "Error City Not Found");
            // should not need this as api checks. kept just in case
        }
        mResponse = theResult;
        setCurrent();
        setHourly();
        setDaily();
    }

    /**
     * sets current weather
     *
     */
    private void setCurrent() {
        try {
            JSONObject current = mResponse.getJSONObject("current");
            JSONObject currentWeather = current.getJSONArray("weather").getJSONObject(0);
            mCurrentWeather.setValue(
                    new Weather(
                            mResponse.getString("city"),
                            current.getString("dt"),
                            current.getString("temp"),
                            setEachWordCap(currentWeather.getString("description")),
                            currentWeather.getString("icon")
                    ));
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success Weather Current");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * sets hourly weather
     *
     */
    private void setHourly() {
        ArrayList<Weather> weatherList = new ArrayList<>();
        try {
            JSONArray hourly = mResponse.getJSONArray("hourly");
            for (int i = 0; i < hourly.length(); i++) {
                JSONObject hour = hourly.getJSONObject(i);
                JSONObject weather = hour.getJSONArray("weather").getJSONObject(0);
                //formatting date to hour:00
                Date date = new Date((Long.parseLong(hour.getString("dt")) + Long.parseLong(mResponse.getString("timezone_offset"))) * 1000);
                DateFormat formatter = new SimpleDateFormat("hh", Locale.US);
                formatter.setTimeZone(TimeZone.getTimeZone("UTC-7"));
                String dateFormatted = formatter.format(date) + ":00";
                //adding object to list
                weatherList.add(
                        new Weather(
                                mResponse.getString("city"),
                                dateFormatted,
                                hour.getString("temp"),
                                setEachWordCap(weather.getString("description")),
                                weather.getString("icon")
                        ));
            }
            mHourlyWeather.setValue(weatherList);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success Weather Hourly");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * sets daily weather
     *
     */
    private void setDaily() {
        ArrayList<Weather> weatherList = new ArrayList<>();
        try {
            JSONArray daily = mResponse.getJSONArray("daily");
            for (int i = 0; i < daily.length(); i++) {
                JSONObject day = daily.getJSONObject(i);
                JSONObject temp = day.getJSONObject("temp");
                JSONObject weather = day.getJSONArray("weather").getJSONObject(0);
                //formatting date to M/D
                Date date = new Date(Long.parseLong(day.getString("dt")) * 1000);
                DateFormat formatter = new SimpleDateFormat("MM/dd", Locale.US);
                //adding object to list
                weatherList.add(
                        new Weather(
                                mResponse.getString("city"),
                                formatter.format(date),
                                temp.getString("day"),
                                temp.getString("night"),
                                setEachWordCap(weather.getString("description")),
                                weather.getString("icon")
                        ));
            }
            mDailyWeather.setValue(weatherList);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success Weather Daily");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * returns a string with each word having a starting capital letter
     *
     * @param theString String the string to get capitalized
     * @return a string with each word having a starting capital letter
     */
    private String setEachWordCap(String theString) {
        String[] words = theString.split("\\s");
        StringBuilder cap = new StringBuilder();
        for(String w: words){
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            cap.append(first.toUpperCase()).append(afterFirst).append(" ");
        }
        return cap.toString().trim();
    }
}
