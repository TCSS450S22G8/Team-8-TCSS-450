package edu.uw.tcss450.group8.chatapp.ui.comms.connection;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.auth0.android.jwt.JWT;

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
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherViewModel;


/**
 * View Model for contact list.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/17/22
 */
public class ContactListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContact;
    private MutableLiveData<Boolean> mUnFriend;


    /**
     * Constructor for Contact List ViewModel
     * @param application app
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContact = new MutableLiveData<>();
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addContactsListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Contact>> observer) {
        mContact.observe(owner, observer);
        mUnFriend = new MutableLiveData<>();
        mUnFriend.setValue(false);

    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addUnFriendObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Boolean> observer) {
        mUnFriend.observe(owner, observer);
    }

    public void getContacts(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/retrieve";
        Request request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetContactSuccess,
                this::handleGetContactError) {
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

//    public void getContacts(String jwt) {
//        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/retrieve/" + jwt;
//        Request request = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                this::handleGetContactSuccess,
//                this::handleGetContactError); {
//        }
//        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
//                .addToRequestQueue(request);
//    }

    public void unfriend(String jwt, String email) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/delete";
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                this::handleUnfriendSuccess,
                this::handleUnfriendError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt.toString());
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    private void handleUnfriendError(VolleyError volleyError) {
        if (Objects.isNull(volleyError.networkResponse)) {
            Log.e("NETWORK ERROR", volleyError.getMessage());
        }
        else {
            String data = new String(volleyError.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    volleyError.networkResponse.statusCode + " " + data);
        }
        mUnFriend.setValue(false);
    }

    private void handleUnfriendSuccess(final JSONObject response) {
        mUnFriend.setValue(true);
    }

    private void handleGetContactError(VolleyError volleyError) {
        if (Objects.isNull(volleyError.networkResponse)) {
            Log.e("NETWORK ERROR", volleyError.getMessage());
        }
        else {
            String data = new String(volleyError.networkResponse.data, Charset.defaultCharset());
            mContact.setValue(new ArrayList<>());
            Log.e("CLIENT ERROR",
                    volleyError.networkResponse.statusCode + " " + data);
        }
    }

    private void handleGetContactSuccess(final JSONArray obj) {
        ArrayList<Contact> list = new ArrayList<>();
        try {
            for (int i = 0; i <  obj.length(); i++) {
                JSONObject contact = obj.getJSONObject(i);
                list.add(new Contact(
                        contact.getString("username"),
                        contact.getString("email")));
            }
            mContact.setValue(list);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }
}