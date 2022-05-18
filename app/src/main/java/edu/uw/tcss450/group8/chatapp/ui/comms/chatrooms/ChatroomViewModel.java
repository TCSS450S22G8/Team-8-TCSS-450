package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;




/**
 * View Model for message list.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 1.0
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
        mChatroomList.setValue(ChatroomGenerator.getChatroomList());
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer message list
     */
    public void addBlogListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Chatroom>> observer) {
        mChatroomList.observe(owner, observer);
    }


//    /**
//     * sends a JSON request for all the chats the user is in
//     *
//     * @param theZipcode String the zipcode of the desired location
//     */
//    public void getChatsForUser(final String theZipcode) {
//        String url = "https://tcss-450-sp22-group-8.herokuapp.com/weather/zipcode/" + theZipcode;
//        //String url = "http://10.0.2.2:5000/weather/zipcode";
//        //create request
//        Request<JSONObject> request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null,
//                this::handleResult,
//                this::handleErrorZipcode);
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);
//    }


}
