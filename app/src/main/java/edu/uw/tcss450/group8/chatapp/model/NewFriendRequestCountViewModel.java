package edu.uw.tcss450.group8.chatapp.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Notification system for new messages.
 *
 * @author Charles Bryan
 */
public class NewFriendRequestCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewFriendRequestCount;
    // change to a map, apply the chatroom id to it with the count

    public NewFriendRequestCountViewModel() {
        mNewFriendRequestCount = new MutableLiveData<>();
        mNewFriendRequestCount.setValue(0);
    }

    public void addFriendRequestCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewFriendRequestCount.observe(owner, observer);
    }

    public void increment() {
        mNewFriendRequestCount.setValue(mNewFriendRequestCount.getValue() + 1);
    }

    public void reset() {
        mNewFriendRequestCount.setValue(0);
    }
}
