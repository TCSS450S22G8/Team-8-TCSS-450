package edu.uw.tcss450.group8.chatapp.ui.location;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.group8.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.group8.chatapp.ui.weather.Weather;

/**
 * view model to get all user saved locations and add/delete locations.
 *
 * @author shilnara dam
 * @version 5/28/22
 */
public class LocationListViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Location>> mLocations;
    private final MutableLiveData<String> mAdd;
    private final MutableLiveData<String> mDelete;


    /**
     * constructor to instantiate the view model
     *
     * @param application the application
     */
    public LocationListViewModel(@NonNull Application application) {
        super(application);
        mLocations = new MutableLiveData<>();
        mAdd = new MutableLiveData<>();
        mDelete = new MutableLiveData<>();
    }

    /**
     * adds an observer to this live data for the variable mLocations.
     *
     * @param owner LifecycleOwner lifecycle owner that controls the observer
     * @param observer Observer the observer that receives events
     */
    public void addLocationsObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super List<Location>> observer) {
        mLocations.observe(owner, observer);
    }


    /**
     * sends a JSON request to get a list of user saved locations
     *
     * @param theJwt String jwt of user
     */
    public void getLocations(String theJwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/location";
        //creating request
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleLocationResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", theJwt);
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * sends a JSON request to add a location
     *
     * @param theJwt String jwt of user
     * @param theLat Double the latitude
     * @param theLon Double the longitude
     */
    public void addLocation(String theJwt, Double theLat, Double theLon) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/location/add/"
                + theLat + "/" + theLon;
        //creating request
        Log.e("stuff", "something ");
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                this::handleAddResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", theJwt);
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * sends a JSON request to delete a location
     *
     * @param theJwt String jwt of user
     * @param theLat Double the latitude
     * @param theLon Double the longitude
     */
    public void deleteLocation(String theJwt, String theLat, String theLon) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/location/delete/"
                + theLat + "/" + theLon;
        //creating request
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                this::handleLocationResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", theJwt);
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }





    /**
     * handler for successful add locations request
     *
     * @param theResult JSONObject of resulting request
     */
    private void handleAddResult(final JSONObject theResult) {
        mAdd.setValue("yes");
        Log.e("stuff", theResult.toString());
    }


    /**
     * handler for successful delete locations request
     *
     * @param theResult JSONObject of resulting request
     */
    private void handleDeleteResult(final JSONObject theResult) {
        mDelete.setValue("yes");
    }


    /**
     * handler for successful get locations request
     *
     * @param theResult JSONObject of resulting request
     */
    private void handleLocationResult(final JSONObject theResult) {
        ArrayList<Location> locationList = new ArrayList<>();
        try {
            JSONArray locations = theResult.getJSONArray("locations");
            for (int i = 0; i < locations.length(); i++) {
                JSONObject single = locations.getJSONObject(i);
                locationList.add(new Location(
                        single.getString("nickname"),
                        single.getString("lat"),
                        single.getString("long")
                ));
            }
            mLocations.setValue(locationList);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success for getting locations");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * handler request errors
     *
     * @param theError VolleyError request error
     */
    private void handleError(final VolleyError theError) {
        Log.e("stuff", theError.toString());
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
}
