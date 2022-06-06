package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.remove;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.Request;
import com.android.volley.VolleyError;
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
 * View Model for contact list in chatroom remove users.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @version 6/5/22
 */
public class ChatroomRemoveListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContact;
    private MutableLiveData<Integer> mChatid;
    private MutableLiveData<String> mjwt;
    private MutableLiveData<List<String>> mNames;
    private MutableLiveData<List<Contact>> mGetContactsNot;

    /**
     * Constructor for Chatroom remove users List ViewModel
     *
     * @param application app
     */
    public ChatroomRemoveListViewModel(@NonNull Application application) {
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
    public void addChatroomRemoveListObserver(@NonNull LifecycleOwner owner,
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
     * Endpoint call to do first step of removing users to chat
     *  @param jwt String of jwt
     * @param namesToRemove list of string with emails of who to remove
     * @param chatId The id of the chat to add too
     */
    public void remove1(String jwt, List<String> namesToRemove, int chatId) {
        for(int j = 0; j < namesToRemove.size();j++) {
            mjwt.setValue(jwt);
            mNames.setValue(namesToRemove);
            mChatid.setValue(chatId);
            String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/delete/user/"+chatId+"/"+namesToRemove.get(j);
            JSONObject body = new JSONObject();
            try {
                body.put("chatId", chatId);
                body.put("email", namesToRemove.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request<JSONObject> request = new JsonObjectRequest(
                    Request.Method.DELETE,
                    url,
                    body,
                    this::handleRemove1Success,
                    this::handleRemove1Error) {
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
     * Handles errors for first remove user endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleRemove1Error(VolleyError volleyError) {
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
     * Handles the success for first remove user endpoint calls
     *
     * @param response JSONObject
     */
    private void handleRemove1Success(final JSONObject response) {
    }


    /**
     * Endpoint get the users in a chat who are currently in it
     *  @param jwt String of jwt
     * @param chatId The id of the chat to add too
     */
    public void getContacts(String jwt, int chatId) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/"+chatId;
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetContactsSuccess,
                this::handleGetContactsError) {
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
     * Handles errors for users in contacts endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleGetContactsError(VolleyError volleyError) {
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
     * Handles the success for get users in contacts endpoint calls
     *
     * @param response JSONObject
     */
    private void handleGetContactsSuccess(final JSONObject response) {
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


}