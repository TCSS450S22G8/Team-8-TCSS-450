package edu.uw.tcss450.group8.chatapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group8.chatapp.ui.weather.Weather;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherHourlyRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherViewModel;


/**
 * Class for user home page.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @version 5/19/22
 */
public class HomeFragment extends Fragment {

    private WeatherViewModel mWeatherModel;
    private FragmentHomeBinding mBinding;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        mWeatherModel.getWeatherZipCode("98404");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentHomeBinding.inflate(inflater);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWeatherModel.addCurrentWeatherObserver(
                getViewLifecycleOwner(),
                this::observeCurrentWeatherResponse);
        mWeatherModel.addHourlyWeatherObserver(this,
                weatherList ->
                        mBinding.listWeatherHourly.setAdapter(new WeatherHourlyRecyclerViewAdapter(weatherList)));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Observer for current weather
     *
     * @param theWeather Weather
     */
    private void observeCurrentWeatherResponse(final Weather theWeather) {
        mBinding.textCurrentCondition.setText(theWeather.getCondition());
    }
}
