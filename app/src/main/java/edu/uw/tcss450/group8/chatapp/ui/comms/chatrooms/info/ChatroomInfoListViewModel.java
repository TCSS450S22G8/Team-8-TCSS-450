package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.info;

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
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;


/**
 * View Model for contact list in chatroom add users.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @version 6/2/22
 */
public class ChatroomInfoListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContact;
    private MutableLiveData<Integer> mChatid;
    private MutableLiveData<String> mjwt;
    private MutableLiveData<List<String>> mNames;
    private MutableLiveData<List<Contact>> mGetContactsNot;

    /**
     * Constructor for Chatroom info List ViewModel
     *
     * @param application app
     */
    public ChatroomInfoListViewModel(@NonNull Application application) {
        super(application);
        mContact = new MutableLiveData<>();
        mChatid = new MutableLiveData<>();
        mjwt = new MutableLiveData<>();
        mNames = new MutableLiveData<>();
        mGetContactsNot = new MutableLiveData<>();
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addChatroomInfoListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Contact>> observer) {
        mContact.observe(owner, observer);
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addGetContactsNotObserver(@NonNull LifecycleOwner owner,
                                               @NonNull Observer<? super List<Contact>> observer) {
        mGetContactsNot.observe(owner, observer);
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

    /**
     * Endpoint call to do first step of adding users to chat
     *  @param jwt String of jwt
     * @param namesToAdd list of string with emails of who to add
     * @param chatId The id of the chat to add too
     */
    public void add1(String jwt, List<String> namesToAdd, int chatId) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/addOther/"+chatId;
        for(int j = 0; j < namesToAdd.size();j++) {
            mjwt.setValue(jwt);
            mNames.setValue(namesToAdd);
            mChatid.setValue(chatId);
            JSONObject body = new JSONObject();
            try {
                body.put("chatId", chatId);
                body.put("email", namesToAdd.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request<JSONObject> request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    body,
                    this::handleAdd1Success,
                    this::handleAdd1Error) {
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


    /**
     * Handles errors for first add user endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleAdd1Error(VolleyError volleyError) {
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
     * Handles the success for first add user endpoint calls
     *
     * @param response JSONObject
     */
    private void handleAdd1Success(final JSONObject response) {
    }


    /**
     * Endpoint get the users in a chat who are not currently in it
     *  @param jwt String of jwt
     * @param chatId The id of the chat to add too
     */
    public void getContactsNot(String jwt, int chatId) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/"+chatId;
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetContactsNotSuccess,
                this::handleGetContactsNotError) {
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
     * Handles errors for users not in contacts endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleGetContactsNotError(VolleyError volleyError) {
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
     * Handles the success for get users not in contacts endpoint calls
     *
     * @param response JSONObject
     */
    private void handleGetContactsNotSuccess(final JSONObject response) {
        ArrayList<Contact> list = new ArrayList<>();
        try {
            JSONArray temp = response.getJSONArray("rows");
            for (int i = 0; i <  temp.length(); i++) {
                JSONObject contact = temp.getJSONObject(i);
                list.add(new Contact(
                        contact.getString("username"),
                        contact.getString("email")));
            }
            mGetContactsNot.setValue(list);
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
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
     * @param obj returned json object
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