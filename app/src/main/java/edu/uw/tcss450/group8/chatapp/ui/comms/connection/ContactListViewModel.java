package edu.uw.tcss450.group8.chatapp.ui.comms.connection;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.List;




/**
 * View Model for contact list.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @version 1.0
 */
public class ContactListViewModel extends AndroidViewModel {
    private MutableLiveData<List<Contact>> mContactList;

    /**
     * Constructor for Contact List ViewModel
     * @param application
     */
    public ContactListViewModel(@NonNull Application application) {
        super(application);
        mContactList = new MutableLiveData<>();
        mContactList.setValue(ContactGenerator.getContactList());
    }

    /**
     * Helper method for observe
     * @param owner owner of lifecycle
     * @param observer contact list
     */
    public void addBlogListObserver(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<Contact>> observer) {
        mContactList.observe(owner, observer);
    }


}