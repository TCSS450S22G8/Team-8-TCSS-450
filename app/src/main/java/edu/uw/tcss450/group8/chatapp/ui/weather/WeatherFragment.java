package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import com.squareup.picasso.Picasso;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherBinding;

/**
 * Class for Weather Fragment to display weather
 *
 *  @author Shilnara Dam
 *  @version 5/15/22
 */
public class WeatherFragment extends Fragment {
    private WeatherViewModel mWeatherModel;
    private FragmentWeatherBinding mBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        //hardcoded zipcode
        String zipcode = "98404";
        mWeatherModel.getWeatherZipCode(zipcode);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = FragmentWeatherBinding.bind(requireView());
        mBinding.progressBar.setVisibility(View.VISIBLE);
        //adding weather observers
        mWeatherModel.addCurrentWeatherObserver(
                getViewLifecycleOwner(),
                this::observeCurrentWeatherResponse);
        mWeatherModel.addErrorObserver(getViewLifecycleOwner(),
                this::observeErrorResponse);
        mWeatherModel.addHourlyWeatherObserver(getViewLifecycleOwner(),
                weatherList ->
                    mBinding.listWeatherHourly.setAdapter(new WeatherHourlyRecyclerViewAdapter(weatherList)));
        mWeatherModel.addDailyWeatherObserver(getViewLifecycleOwner(),
                weatherList -> {
                    mBinding.listWeatherDaily.setAdapter(new WeatherDailyRecyclerViewAdapter(weatherList));
        });
        //button listener for zipcode
        mBinding.buttonWeatherZipcodeEnter.setOnClickListener(button -> {
            try {
                //remove text focus
                mBinding.editWeatherZipcode.clearFocus();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard did not close");
            }
            setDataToBlank();
            mWeatherModel.getWeatherZipCode(mBinding.editWeatherZipcode.getText().toString());
        });
        //button listener for lat/lon
        mBinding.buttonWeatherLatLonEnter.setOnClickListener(button -> {
            try {
                //remove text focus
                mBinding.editWeatherLat.clearFocus();
                mBinding.editWeatherLon.clearFocus();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard did not close");
            }
            setDataToBlank();
            mWeatherModel.getWeatherLatLon(mBinding.editWeatherLat.getText().toString(),
                    mBinding.editWeatherLon.getText().toString());

        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * An observer on the HTTP Response from the web server.
     * updates current weather info
     *
     * @param theWeather the Response from the server
     */
    private void observeCurrentWeatherResponse(final Weather theWeather) {
        //making elements visible
        mBinding.textWeatherCity.setVisibility(View.VISIBLE);
        mBinding.textWeatherCurrentTemp.setVisibility(View.VISIBLE);
        mBinding.textWeatherCondition.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);

        //setting current weather data
        mBinding.textWeatherDailyHeader.setVisibility(View.VISIBLE);
        mBinding.textWeatherHourlyHeader.setVisibility(View.VISIBLE);
        mBinding.listWeatherHourly.setVisibility(View.VISIBLE);
        mBinding.listWeatherDaily.setVisibility(View.VISIBLE);
        mBinding.imageWeatherCondition.setVisibility(View.VISIBLE);
        mBinding.textWeatherCity.setText(theWeather.getCity());
        Picasso.get().load(theWeather.getIcon()).into(mBinding.imageWeatherCondition);
        mBinding.textWeatherCurrentTemp.setText(theWeather.getTemp());
        mBinding.textWeatherCondition.setText(theWeather.getCondition());


    }

    /**
     * An observer on the HTTP Response from the web server.
     * if user input incorrect data then show past correct data
     *
     * @param theError String the Response from the server
     */
    private void observeErrorResponse(final String theError) {
        //making elements visible again due to incorrect input
        mBinding.textWeatherDailyHeader.setVisibility(View.VISIBLE);
        mBinding.textWeatherHourlyHeader.setVisibility(View.VISIBLE);
        mBinding.listWeatherHourly.setVisibility(View.VISIBLE);
        mBinding.listWeatherDaily.setVisibility(View.VISIBLE);
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.textWeatherCity.setVisibility(View.VISIBLE);
        mBinding.imageWeatherCondition.setVisibility(View.VISIBLE);
        mBinding.textWeatherCurrentTemp.setVisibility(View.VISIBLE);
        mBinding.textWeatherCondition.setVisibility(View.VISIBLE);
        //checking which type of data error
        switch (theError) {
            case "zipcode":
                mBinding.editWeatherZipcode.setError("Invalid Zipcode");
                break;
            case "lat/lon":
                mBinding.editWeatherLat.setError("Invalid Latitude");
                mBinding.editWeatherLon.setError("Invalid Longitude");
        }
    }

    /**
     * when getting new weather info, all current data should be set to blank
     */
    private void setDataToBlank() {
        mBinding.textWeatherDailyHeader.setVisibility(View.GONE);
        mBinding.textWeatherHourlyHeader.setVisibility(View.GONE);
        mBinding.listWeatherHourly.setVisibility(View.GONE);
        mBinding.listWeatherDaily.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.textWeatherCity.setVisibility(View.INVISIBLE);
        mBinding.imageWeatherCondition.setVisibility(View.INVISIBLE);
        mBinding.textWeatherCurrentTemp.setVisibility(View.INVISIBLE);
        mBinding.textWeatherCondition.setVisibility(View.INVISIBLE);
    }
}