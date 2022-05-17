package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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


}
