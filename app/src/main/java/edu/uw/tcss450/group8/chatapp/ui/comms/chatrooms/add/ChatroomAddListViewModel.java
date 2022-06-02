package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add;

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
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.Chatroom;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;


/**
 * View Model for contact list in chatroom add.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @version 6/2/22
 */
public class ChatroomAddListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContact;
    private MutableLiveData<Integer> mChatid;
    private MutableLiveData<String> mjwt;
    private MutableLiveData<List<String>> mNames;
    private String mName;
    private String mEmail;
    private MutableLiveData<List<Chatroom>> mChatroomsList;
    private ArrayList<Chatroom> mChatrooms;


    /**
     * Constructor for Chatroom add List ViewModel
     * @param application app
     */
    public ChatroomAddListViewModel(@NonNull Application application) {
        super(application);
        mContact = new MutableLiveData<>();
        mChatid = new MutableLiveData<>();
        mjwt = new MutableLiveData<>();
        mNames = new MutableLiveData<>();
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addChatroomAddListObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super List<Contact>> observer) {
        mContact.observe(owner, observer);
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
     * Endpoint call to do first step of adding chat (create room)
     *  @param jwt String of jwt
     * @param name String of chatroom name wanted
     * @param namesToAdd list of string with emails of who to add
     */
    public void add1(String jwt, String name, List<String> namesToAdd, String email, MutableLiveData<List<Chatroom>> chatrooms, ArrayList<Chatroom> chatroomlist) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats";
        mjwt.setValue(jwt);
        mNames.setValue(namesToAdd);
        mName = name;
        mEmail = email;
        mChatroomsList = chatrooms;
        mChatrooms = chatroomlist;
        JSONObject body = new JSONObject();
        try {
            body.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.POST,
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

    /**
     * Endpoint call to do second step of adding chat (add yourself)
     *
     * @param chatId the chatId you want to add yourself and others to
     */
    public void add2(int chatId) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/addSelf/"+chatId;
        JSONObject body = new JSONObject();
        try {
            body.put("chatId", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                this::handleAdd2Success,
                this::handleAdd2Error) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", mjwt.getValue());
                return headers;
            }
        };
        RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                .addToRequestQueue(request);
    }

    /**
     * Endpoint call to do third step of adding chat (add other users)
     *
     * @param namesToAdd list of string with emails of who to add
     */
    public void add3(List<String> namesToAdd) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/addOther/"+mChatid.getValue();
        for(int j = 0; j < namesToAdd.size();j++) {
            JSONObject body = new JSONObject();
            try {
                body.put("chatId", mChatid.getValue());
                body.put("email", namesToAdd.get(j));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Request<JSONObject> request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    body,
                    this::handleAdd3Success,
                    this::handleAdd3Error) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    // add headers <key,value>
                    headers.put("Authorization", mjwt.getValue());
                    return headers;
                }
            };
            RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                    .addToRequestQueue(request);
        }
    }



    /**
     * Handles errors for first add endpoint calls
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
     * Handles errors for second add endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleAdd2Error(VolleyError volleyError) {
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
     * Handles errors for third add endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleAdd3Error(VolleyError volleyError) {
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
     * Handles the success for first add endpoint calls
     *
     * @param response JSONObject
     */
    private void handleAdd1Success(final JSONObject response) {
        try {
            int chatId = response.getInt("chatID");
            mChatid.setValue(chatId);
            add2(chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the success for second add endpoint calls
     *
     * @param response JSONObject
     */
    private void handleAdd2Success(final JSONObject response) {
        mChatrooms.add(new Chatroom(mChatid.getValue().toString(),mName,mEmail));
        mChatroomsList.setValue(mChatrooms);
        add3(mNames.getValue());
    }

    /**
     * Handles the success for third add endpoint calls
     *
     * @param response JSONObject
     */
    private void handleAdd3Success(final JSONObject response) {

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