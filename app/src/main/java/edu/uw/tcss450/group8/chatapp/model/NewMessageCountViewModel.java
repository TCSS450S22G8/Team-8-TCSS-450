package edu.uw.tcss450.group8.chatapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Notification system for new messages.
 *
 * @author Charles Bryan
 */
public class NewMessageCountViewModel extends ViewModel implements Serializable {
    private Map<Integer, MutableLiveData<Integer>> mNewMessageCount;
    private MutableLiveData<Integer> mCount;


    // change to a map, apply the chatroom id to it with the count

    public NewMessageCountViewModel() {
        mNewMessageCount = new HashMap<>();
        mCount = new MutableLiveData<>(0);
    }

    public void addNewMessageCountObserver(@NonNull LifecycleOwner owner,
                                           @NonNull Observer<? super Integer> observer) {
        mCount.observe(owner, observer);
    }


    public void addMessageCountObserver(int chatId, @NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {

        if (!mNewMessageCount.containsKey(chatId)) {
            mNewMessageCount.put(chatId, new MutableLiveData<>(0));
        }
        mNewMessageCount.get(chatId).observe(owner, observer);
    }


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

    public void clear(int chatId) {
        if (mNewMessageCount.containsKey(chatId)) {
            mNewMessageCount.get(chatId).setValue(0);
        }
        update();
    }

    public void update() {
        int i = mNewMessageCount.keySet().stream().filter(m -> m != -1).mapToInt(m -> mNewMessageCount.get(m).getValue()).sum();
        mCount.setValue(i);
    }
}
