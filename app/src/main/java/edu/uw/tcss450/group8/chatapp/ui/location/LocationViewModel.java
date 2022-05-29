package edu.uw.tcss450.group8.chatapp.ui.location;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * view model that retains information on the user's location
 * Adapted from original code by Charles Bryan.
 * @author Charles Bryan
 */
public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;

    /**
     * constructor for the view model
     */
    public LocationViewModel() {
        mLocation = new MediatorLiveData<>();
    }

    /**
     * add observer for location updates
     *
     * @param owner LifecycleOwner lifecycle owner
     * @param observer Observer observer that will do something based on location change
     */
    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    /**
     * updates/sets the location
     *
     * @param location Location the location to set
     */
    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    /**
     * gets the current location
     *
     * @return current location
     */
    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }
}
