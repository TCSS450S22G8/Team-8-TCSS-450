package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

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
import com.android.volley.toolbox.Volley;
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

    /**
     * Constructor for Message List ViewModel
     * @param application
     */
    public ChatroomViewModel(@NonNull Application application) {
        super(application);
        mChatroomList = new MutableLiveData<>();
//        mChatroomList.setValue(ChatroomGenerator.getChatroomList());
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
     * do it
     * @param chatRooms
     */
    public void handleGetChatRoomSuccess(final JSONArray chatRooms) {
        ArrayList<Chatroom> listChatRooms = new ArrayList<>();
        try {
            for (int i = 0; i < chatRooms.length(); i++) {
                JSONObject temp = chatRooms.getJSONObject(i);
                listChatRooms.add(new Chatroom(temp.getString("chatid"),temp.getString("name")));
            }
            mChatroomList.setValue(listChatRooms);
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

}
