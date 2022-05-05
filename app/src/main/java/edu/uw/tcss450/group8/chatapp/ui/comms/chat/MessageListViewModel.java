package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

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
 * @author JenHo Liao
 * @version 1.0
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
