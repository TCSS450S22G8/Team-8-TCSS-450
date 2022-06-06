package edu.uw.tcss450.group8.chatapp.ui.settings;

import android.app.Application;
import android.util.Base64;
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
import java.util.Map;
import java.util.Objects;

import edu.uw.tcss450.group8.chatapp.io.RequestQueueSingleton;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.Chatroom;

/**
 * Class View Model for Login Fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Sean Logan
 * @author Shilnara Dam
 * @version 5/19/12
 */
public class SettingViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<String> mFirstName;
    private MutableLiveData<String> mLastName;
    private MutableLiveData<String> mUserName;
    private MutableLiveData<String> mFlag;


    /**
     * Instantiates Login View Model
     *
     * @param application top level application
     */
    public SettingViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mFirstName = new MutableLiveData<>();
        mLastName = new MutableLiveData<>();
        mUserName = new MutableLiveData<>();
        mFlag = new MutableLiveData<>();
    }

    /**
     * Adds response to fragment.
     *
     * @param owner    owner
     * @param observer observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Adds response to fragment.
     *
     * @param owner    owner
     * @param observer observer
     */
    public void addUserFirstNameObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super String> observer) {
        mFirstName.observe(owner, observer);
    }

    /**
     * Adds response to fragment.
     *
     * @param owner    owner
     * @param observer observer
     */
    public void addUserLastNameObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super String> observer) {
        mLastName.observe(owner, observer);
    }

    /**
     * resets flag for deleting account
     */
    public void resetFlag() {
        mFlag = new MutableLiveData<>();
    }

    /**
     * Adds response to fragment.
     *
     * @param owner    owner
     * @param observer observer
     */
    public void addUserNameObserver(@NonNull LifecycleOwner owner,
                                         @NonNull Observer<? super String> observer) {
        mUserName.observe(owner, observer);
    }

    /**
     * Handling JSON Request Errors
     *
     * @param error error that is given
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError 1st");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError 2nd");
            }
        }
    }

    /**
     * Handles success for getting user info
     *
     * @param userInfo
     */
    public void handleSuccess(final JSONObject userInfo) {
        try {
            mUserName.setValue(userInfo.getString("username"));
            mFirstName.setValue(userInfo.getString("firstname"));
            mLastName.setValue(userInfo.getString("lastname"));
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    public void handleDeleteAccountSuccess(final JSONObject userInfo) {
        mFlag.setValue("yes");
    }

    /**
     *
     */
    public void addObserverForFlag(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super String> observer) {
        mFlag.observe(owner, observer);
    }

    /**
     * Sends users JWT to delete push_token from database on logout
     *
     * @param jwt    String user JWT
     */
    public void connect(final String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/pushyregister" + jwt;
        Request request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null, //no body for this get request
                mResponse::setValue,
                this::handleError) {
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
     * sends a JSON request for user information
     *
     * @param jwt String the users jwt
     */
    public void getUserInfo(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/user-info/";

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleSuccess,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


    /**
     * sends a JSON request to delete users account
     *
     * @param jwt String the users jwt
     */
    public void deleteUserAccount(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/delete-account/";

        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                this::handleDeleteAccountSuccess,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }


}
