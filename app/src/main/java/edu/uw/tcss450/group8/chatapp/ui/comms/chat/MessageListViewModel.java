/**
 * @author jliaoh
 * @version 1.0
 */
package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View Model for message list
 */
public class MessageListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Message>> mMessageList;

    /**
     * Constructor for Message List ViewModel
     * @param application
     */
    public MessageListViewModel(@NonNull Application application) {
        super(application);
        mMessageList = new MutableLiveData<>();
        mMessageList.setValue(MessageGenerator.getMessageList());
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer message list
     */
    public void addBlogListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Message>> observer) {
        mMessageList.observe(owner, observer);
    }


}
