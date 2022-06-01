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
 * @version 5/19/22
 */
public class ChatroomViewModel extends AndroidViewModel {
    private MutableLiveData<List<Chatroom>> mChatroomList;
    private ChatroomListFragment mParent;
    private ArrayList<Chatroom> mChatrooms;
    private String mjwt;
    private String mEmail;
    private int mChatId;
    //private FragmentChatroomListBinding mBinding;
    //private ChatroomListFragment mBinding2;

    /**
     * Constructor for Message List ViewModel
     * @param application
     *
     */
    public ChatroomViewModel(@NonNull Application application) {
        super(application);
        mChatroomList = new MutableLiveData<>();
        //mBinding = application;
        //mParent =
//        mChatroomList.setValue(ChatroomGenerator.getChatroomList());
    }

    /**
     * send
     *
     *
     * @return
     */
    public MutableLiveData<List<Chatroom>> getChatRooms() {
        return mChatroomList;
    }

    public ArrayList<Chatroom> getChatRoomsList() {
        return mChatrooms;
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer message list
     */
    public void addChatRoomListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Chatroom>> observer) {
        mChatroomList.observe(owner, observer);
    }

    /**
     * sends a JSON request for all the chats the user is in
     *
     * @param jwt String the zipcode of the desired location
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
     * do it
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
     * do it
     * @param volleyError
     */
    public void handlesGetChatRoomError(VolleyError volleyError) {
        if (Objects.isNull(volleyError.networkResponse)) {
            Log.e("NETWORK ERROR", volleyError.getMessage());
        }
        else {
            String data = new String(volleyError.networkResponse.data, Charset.defaultCharset());
            mChatroomList.setValue(new ArrayList<>());
            Log.e("CLIENT ERROR",
                    volleyError.networkResponse.statusCode + " " + data);
        }
    }

    /**
     * Endpoint call to do first step of adding chat (create room)
     *  @param jwt String of jwt
     *
     */
    public void attemptRemoveSelf1(String jwt, int chatId, String email) {
        //mParent = parent;
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
        //mParent = FragmentChatroomListBinding.bind(mBinding2.getView());
        //mBinding.swipeContactsRefresh.setRefreshing(false);
        //Toast.makeText(mBinding2.getActivity(), "Chat Deleted!", Toast.LENGTH_SHORT).show();
        //mParent.refreshAdapter();

    }

    /**
     * Endpoint call to do first step of adding chat (create room)
     *  @param jwt String of jwt
     *
     */
    public void attemptRemoveSelf2(String jwt, int chatId, String email) {
        //mParent = parent;
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
     * Handles errors for third add endpoint calls
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
     * Endpoint call to do first step of adding chat (create room)
     *  @param jwt String of jwt
     *
     */
    public void attemptGetUsersRoom(String jwt, int chatId, String email) {
        //mParent = parent;
        mjwt = jwt;
        mChatId = chatId;
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
     * Handles errors for third add endpoint calls
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
            Log.e("NumInChat", ": "+members);
            if(members == 1){
                attemptRemoveSelf2(mjwt,mChatId,mEmail);
            }
            else{
                attemptRemoveSelf1(mjwt,mChatId,mEmail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
