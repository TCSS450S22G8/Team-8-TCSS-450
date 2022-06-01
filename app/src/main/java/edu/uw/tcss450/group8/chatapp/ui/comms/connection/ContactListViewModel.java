package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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


/**
 * View Model for contact list.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/19/22
 */
public class ContactListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContact;
    private MutableLiveData<List<Contact>> mNonContact;
    private MutableLiveData<List<Contact>> mIncomingRequest;
    private MutableLiveData<List<Contact>> mOutgoingRequest;


    /**
     * Constructor for Contact List ViewModel
     * @param application app
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContact = new MutableLiveData<>();
        mNonContact = new MutableLiveData<>();
        mIncomingRequest = new MutableLiveData<>();
        mOutgoingRequest = new MutableLiveData<>();

    }

    public List<Contact> getContactList() {
        return mContact.getValue();
    }
    public List<Contact> getNonContactList() {
        return mNonContact.getValue();
    }
    public List<Contact> getIncomingRequestSList() {return  mIncomingRequest.getValue();}
    public List<Contact> getOutgoingRequestSList() {return  mOutgoingRequest.getValue();}

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addContactsListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Contact>> observer) {
        mContact.observe(owner, observer);
    }

    public void addNonContactsListObserver(@NonNull LifecycleOwner owner,
                                           @NonNull Observer<? super List<Contact>> observer) {
        mNonContact.observe(owner, observer);
    }

    public void incomingRequestListObserver(@NonNull LifecycleOwner owner,
                                            @NonNull Observer<? super List<Contact>> observer) {
        mIncomingRequest.observe(owner, observer);
    }

    public void outgoingRequestListObserver(@NonNull LifecycleOwner owner,
                                            @NonNull Observer<? super List<Contact>> observer) {
        mOutgoingRequest.observe(owner, observer);
    }


    /**
     * Endpoint to retrieve contacts of the user
     *
     * @param jwt String
     */
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

    public void getNonFriendList(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/retrieve/nonfriends";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetNonFriendListSuccess,
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

    public void getIncomingRequestList(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/retrieve/incoming";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetIncomingRequestListSuccess,
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

    public void getOutgoingRequestList(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/retrieve/outgoing";
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetOutgoingRequestListSuccess,
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

    private void handleGetOutgoingRequestListSuccess(JSONObject jsonObject) {
        ArrayList<Contact> list = new ArrayList<>();
        try {
            JSONObject outgoing = jsonObject.getJSONObject("outgoing");
            JSONArray rows = outgoing.getJSONArray("rows");
            for (int i = 0; i <  rows.length(); i++) {
                JSONObject contact = rows.getJSONObject(i);
                list.add(new Contact(
                        contact.getString("username"),
                        contact.getString("email")));
                Log.e("outgoing", contact.getString("username"));
            }
            mOutgoingRequest.setValue(list);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    private void handleGetIncomingRequestListSuccess(JSONObject jsonObject) {
        ArrayList<Contact> list = new ArrayList<>();
        try {
            JSONObject incoming = jsonObject.getJSONObject("incoming");
            JSONArray rows = incoming.getJSONArray("rows");
            for (int i = 0; i <  rows.length(); i++) {
                JSONObject contact = rows.getJSONObject(i);
                list.add(new Contact(
                        contact.getString("username"),
                        contact.getString("email")));
                Log.e("incoming", contact.getString("username"));
            }
            mIncomingRequest.setValue(list);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    private void handleGetNonFriendListSuccess(JSONObject obj) {
        ArrayList<Contact> list = new ArrayList<>();
        try {
            JSONArray array = obj.getJSONArray("members");
            for (int i = 0; i <  array.length(); i++) {
                JSONObject contact = array.getJSONObject(i);
                list.add(new Contact(
                        contact.getString("username"),
                        contact.getString("email")));
            }
            mNonContact.setValue(list);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }


    /**
     * Endpoint call to un-friend someone (delete a contact)
     *
     * @param jwt String
     * @param email String
     */
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
    public void addFriend(String jwt, String email) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/add";
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
                this::handleAddFriendSuccess,
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

    public void acceptFriendRequest(String jwt, String email) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/contacts/accept";
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
                this::handleAcceptFriendRequestSuccess,
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

    public void deleteFriendRequest(String jwt, String email) {
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
                this::handleDeleteFriendRequestSuccess,
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

    private void handleDeleteFriendRequestSuccess(JSONObject jsonObject) {
    }


    private void handleAcceptFriendRequestSuccess(JSONObject jsonObject) {
    }


    private void handleAddFriendSuccess(JSONObject jsonObject) {

    }



    /**
     * Handles errors for unfriend endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleError(VolleyError volleyError) {
        Log.e("ERR","4");
        if (Objects.isNull(volleyError.networkResponse)) {
            Log.e("NETWORK ERROR", volleyError.getMessage());
        }
        else {
            String data = new String(volleyError.networkResponse.data, Charset.defaultCharset());
            Log.e("CLIENT ERROR",
                    volleyError.networkResponse.statusCode + " " + data);
        }
    }

    /**
     * Handles the success for unfriend endpoint calls
     *
     * @param response JSONObject
     */
    private void handleUnfriendSuccess(final JSONObject response) {

    }

    /**
     * Handles errors for the getContact endpoint calls
     *
     * @param volleyError VolleyError
     */
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

    /**
     * Handles success for getContact endpoint calls
     *
     * @param obj
     */
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