package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * view model class to retain current, hourly, and daily weather.
 * May or may not use this class.
 *
 * @author shiilnara dam
 * @version 2.0
 */
public class WeatherViewModel extends AndroidViewModel {

    /**
     * view model class for weather.
     *
     * @param application application
     */
    public WeatherViewModel(@NonNull Application application) {
        super(application);
    }
}
