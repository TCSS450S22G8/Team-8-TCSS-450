package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherMoreInfoBinding;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationViewModel;

/**
 * Fragment to display additional information of the current weather
 *
 * @author shilnara dam
 * @version 6/5/22
 */
public class WeatherMoreInfoFragment extends Fragment {

    WeatherViewModel mWeatherModel;

    /**
     * required public constructor
     */
    public WeatherMoreInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_more_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWeatherModel.addAdditionalCurrentWeatherObserver(getViewLifecycleOwner(), this::setData);
    }

    /**
     * sets the data to be displayed
     *
     * @param theWeather WeatherAdditionalInfo additional weather object
     */
    private void setData(WeatherAdditionalInfo theWeather) {
        FragmentWeatherMoreInfoBinding mBinding = FragmentWeatherMoreInfoBinding.bind(requireView());
        mBinding.textWeatherWind.setText(theWeather.getWind());
        mBinding.textWeatherHumidity.setText(theWeather.getHumidity());
        mBinding.textWeatherDewPoint.setText(theWeather.getDewPoint());
        mBinding.textWeatherPressure.setText(theWeather.getPressure());
        mBinding.textWeatherUvi.setText(theWeather.getUVIndex());
        mBinding.textWeatherClouds.setText(theWeather.getCloudiness());
        mBinding.textWeatherSunrise.setText(theWeather.getSunrise());
        mBinding.textWeatherSunset.setText(theWeather.getSunset());

    }
}