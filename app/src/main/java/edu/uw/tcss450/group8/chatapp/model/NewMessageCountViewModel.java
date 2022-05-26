package edu.uw.tcss450.group8.chatapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification system for new messages.
 *
 * @author Charles Bryan
 * @author JenHo Liao
 * @version 5/25/22
 */
public class NewMessageCountViewModel extends ViewModel implements Serializable {
    private Map<Integer, MutableLiveData<Integer>> mNewMessageCount;
    private MutableLiveData<Integer> mCount;


    public NewMessageCountViewModel() {
        mNewMessageCount = new HashMap<>();
        mCount = new MutableLiveData<>(0);
    }

    /**
     * Register as an observer to listen for the total number of unread message.
     *
     * @param owner    the fragments lifecycle owner
     * @param observer the observer
     */
    public void addNewMessageCountObserver(@NonNull LifecycleOwner owner,
                                           @NonNull Observer<? super Integer> observer) {
        mCount.observe(owner, observer);
    }

    /**
     * Register as an observer to listen for unread message for single chatroom
     *
     * @param owner    the fragments lifecycle owner
     * @param observer the observer
     */
    public void addMessageCountObserver(int chatId, @NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {

        if (!mNewMessageCount.containsKey(chatId)) {
            mNewMessageCount.put(chatId, new MutableLiveData<>(0));
        }
        mNewMessageCount.get(chatId).observe(owner, observer);
    }


    /**
     * Increment chatroom unread.
     *
     * @param chatId the id of chatroom
     */
    public void increment(int chatId) {
        if (mNewMessageCount.containsKey(chatId)) {
            mNewMessageCount.get(chatId).setValue(
                    mNewMessageCount.get(chatId).getValue() + 1
            );
        } else {
            mNewMessageCount.put(chatId, new MutableLiveData<>(1));
        }
        update();
    }

    /**
     * clear the unread for a chatroom.
     *
     * @param chatId the id of chatroom
     */
    public void clear(int chatId) {
        if (mNewMessageCount.containsKey(chatId)) {
            mNewMessageCount.get(chatId).setValue(0);
        }
        update();
    }

    /**
     * update to total unread.
     */
    public void update() {
        int i = mNewMessageCount.keySet().stream().filter(m -> m != -1).mapToInt(m -> mNewMessageCount.get(m).getValue()).sum();
        mCount.setValue(i);
    }
}
