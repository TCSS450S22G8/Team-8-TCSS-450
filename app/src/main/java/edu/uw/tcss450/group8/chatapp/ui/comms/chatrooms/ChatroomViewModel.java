package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

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
 * View Model for message list.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 6/2/22
 */
public class ChatroomViewModel extends AndroidViewModel {
    private MutableLiveData<List<Chatroom>> mChatroomList;
    private ArrayList<Chatroom> mChatrooms;
    private String mjwt;
    private String mEmail;
    private MutableLiveData<Integer> mChatId;

    /**
     * Constructor for Message List ViewModel
     * @param application the app
     *
     */
    public ChatroomViewModel(@NonNull Application application) {
        super(application);
        mChatroomList = new MutableLiveData<>();
        mChatId = new MutableLiveData<>();
    }

    /**
     * Getter for list of chatrooms
     *
     * @return the mutable live data of chatroom list
     */
    public MutableLiveData<List<Chatroom>> getChatRooms() {
        return mChatroomList;
    }

    /**
     * Getter for list of chatrooms as Arraylist
     *
     * @return the Arraylist of chatroom list
     */
    public ArrayList<Chatroom> getChatRoomsList() {
        return mChatrooms;
    }

    /**
     * Getter for list of chat Id
     *
     * @return the mutable live data of chat Id
     */
    public MutableLiveData<Integer> getmChatId() {
        return mChatId;
    }

    /**
     * Setter for list of chat Id
     *
     * @param id the chat id
     * @return the set chat Id
     */
    public MutableLiveData<Integer> setmChatId(int id) {
        mChatId.setValue(id);
        return mChatId;
    }

    /**
     * Helper method for observe
     *
     * @param owner    owner of lifecycle
     * @param observer message list
     */
    public void addChatRoomListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Chatroom>> observer) {
        mChatroomList.observe(owner, observer);
    }

    /**
     * Helper method for observe chatId
     * @param owner owner of lifecycle
     * @param observer integer
     */
    public void addChatIdObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super Integer> observer) {
        mChatId.observe(owner, observer);
    }

    /**
     * sends a JSON request for all the chats the user is in
     *
     * @param jwt String the jwt of the user
     */
    public void getChatRoomsForUser(String jwt) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/get-chats";

        Request<JSONArray> request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                this::handleGetChatRoomSuccess,
                this::handlesGetChatRoomError) {
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
     * Handles get chat room success
     *
     * @param chatRooms
     */
    public void handleGetChatRoomSuccess(final JSONArray chatRooms) {
        ArrayList<Chatroom> listChatRooms = new ArrayList<>();
        try {
            for (int i = 0; i < chatRooms.length(); i++) {
                JSONObject temp = chatRooms.getJSONObject(i);
                listChatRooms.add(new Chatroom(temp.getString("chatid"),temp.getString("name"),temp.getString("owner")));
            }
            mChatroomList.setValue(listChatRooms);
            mChatrooms = listChatRooms;
        } catch (JSONException e){
            Log.e("JSON PARSE ERROR", "Found in handle Success");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * Handles get chatroom error
     *
     * @param volleyError
     */
    public void handlesGetChatRoomError(VolleyError volleyError) {
        if (Objects.isNull(volleyError.networkResponse)) {
            Log.e("NETWORK ERROR", volleyError.getMessage());
        } else {
            String data = new String(volleyError.networkResponse.data, Charset.defaultCharset());
            mChatroomList.setValue(new ArrayList<>());
            Log.e("CLIENT ERROR",
                    volleyError.networkResponse.statusCode + " " + data);
        }
    }

    /**
     *  Endpoint to attempt to remove yourself from a chatroom
     *
     *  @param jwt String of jwt
     */
    public void attemptRemoveSelf1(String jwt, int chatId, String email) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/delete/user/"+chatId+"/"+email;
        JSONObject body = new JSONObject();
        try {
            body.put("chatId", chatId);
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body,
                this::handleRemoveSelf1Success,
                this::handleRemoveSelf1Error) {
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
     * Handles errors for third add endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleRemoveSelf1Error(VolleyError volleyError) {
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
    private void handleRemoveSelf1Success(final JSONObject response) {

    }

    /**
     *  Attempts to delete the chat
     *
     *  @param jwt String of jwt
     */
    public void attemptRemoveSelf2(String jwt, int chatId, String email) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/delete/chatroom/group/"+chatId;
        JSONObject body = new JSONObject();
        try {
            body.put("chatId", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                body,
                this::handleRemoveSelf2Success,
                this::handleRemoveSelf2Error) {
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
     * Handles errors for second add endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleRemoveSelf2Error(VolleyError volleyError) {
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
    private void handleRemoveSelf2Success(final JSONObject response) {


    }

    /**
     *  Attempt to get the users in the chatroom
     *
     *  @param jwt String of jwt
     */
    public void attemptGetUsersRoom(String jwt, int chatId, String email) {
        mjwt = jwt;
        Log.e("chatID", ": "+chatId );
        mChatId.setValue(chatId);
        mEmail = email;
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/chats/"+chatId;
        JSONObject body = new JSONObject();
        try {
            body.put("chatId", chatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request<JSONObject> request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                body,
                this::handleGetUsersRoomSuccess,
                this::handleGetUsersRoomError) {
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
     * Handles errors for get user endpoint calls
     *
     * @param volleyError VolleyError
     */
    private void handleGetUsersRoomError(VolleyError volleyError) {
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
    private void handleGetUsersRoomSuccess(final JSONObject response) {
        try {
            int members = response.getInt("rowCount");
            if(members == 1){
                attemptRemoveSelf2(mjwt,mChatId.getValue(),mEmail);
            }
            else{
                attemptRemoveSelf1(mjwt,mChatId.getValue(),mEmail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
