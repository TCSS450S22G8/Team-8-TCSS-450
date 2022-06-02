package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationViewModel;

/**
 * Class for Weather Fragment to display weather
 *
 * @author Shilnara Dam
 * @version 6/1/22
 */
public class WeatherFragment extends Fragment {
    private WeatherViewModel mWeatherModel;
    private LocationListViewModel mLocationListModel;
    private LocationViewModel mLocationModel;
    private FragmentWeatherBinding mBinding;
    private UserInfoViewModel mUserModel;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(requireActivity()).get(WeatherViewModel.class);
        mLocationModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        mLocationListModel = new ViewModelProvider(requireActivity()).get(LocationListViewModel.class);
        mUserModel = new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.botton_saved_location, menu);
        inflater.inflate(R.menu.botton_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.open_saved_location) {


            mLocationListModel.addLocationsObserver(getViewLifecycleOwner(), locations -> {
                if (mLocationListModel.getLocationCount() == 0) {
                    Navigation.findNavController(getView()).navigate(
                            WeatherFragmentDirections
                                    .actionNavWeatherFragmentToLocationMapFragment());
                } else {
                    Navigation.findNavController(getView()).navigate(
                            WeatherFragmentDirections
                                    .actionNavWeatherFragmentToLocationFragment());
                }
            });

            mLocationListModel.resetLocation();
            mLocationListModel.getLocations(mUserModel.getJwt());


        }
        if (id == R.id.open_map) {

            Navigation.findNavController(getView()).navigate(
                    WeatherFragmentDirections
                            .actionNavWeatherFragmentToLocationMapFragment());

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            //get weather info from arguments (saved locations)
            WeatherFragmentArgs args = WeatherFragmentArgs.fromBundle(getArguments());
            mWeatherModel.getWeatherLatLon(args.getLat(),
                    args.getLon(), mUserModel.getJwt());
        } catch (IllegalArgumentException e) {
            //get weather info from user current location
            //adding observer for current location's to set weather
            mLocationModel.addLocationObserver(getViewLifecycleOwner(), location -> {
                mWeatherModel.getWeatherLatLon(String.valueOf(location.getLatitude()),
                        String.valueOf(location.getLongitude()), mUserModel.getJwt());
            });
        }


        mBinding = FragmentWeatherBinding.bind(requireView());
        mBinding.progressBar.setVisibility(View.VISIBLE);


        //adding weather observers
        mWeatherModel.addCurrentWeatherObserver(
                getViewLifecycleOwner(),
                this::observeCurrentWeatherResponse);
        mWeatherModel.addZipErrorObserver(getViewLifecycleOwner(),
                this::observeZipcodeErrorResponse);
        mWeatherModel.addLatLonErrorObserver(getViewLifecycleOwner(),
                this::observeLatLonErrorResponse);
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
                InputMethodManager imm = (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard did not close");
            }
            //setDataToBlank();
            mWeatherModel.getWeatherZipCode(mBinding.editWeatherZipcode.getText().toString(),
                    mUserModel.getJwt());
        });
        //button listener for lat/lon
        mBinding.buttonWeatherLatLonEnter.setOnClickListener(button -> {
            try {
                //remove text focus
                mBinding.editWeatherLat.clearFocus();
                mBinding.editWeatherLon.clearFocus();
                //close keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard did not close");
            }
            mWeatherModel.getWeatherLatLon(mBinding.editWeatherLat.getText().toString(),
                    mBinding.editWeatherLon.getText().toString(), mUserModel.getJwt());
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
    private void observeZipcodeErrorResponse(String theError) {
        //send toast message stating bad zipcode
        Toast.makeText(getActivity(), "Invalid Zipcode!", Toast.LENGTH_SHORT).show();
        mWeatherModel.resetZipcodeError();
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
    }

    /**
     * An observer on the HTTP Response from the web server.
     * if user input incorrect data then show past correct data
     *
     * @param theError String the Response from the server
     */
    private void observeLatLonErrorResponse(String theError) {
         //send toast message stating bad lat long
        Toast.makeText(getActivity(), "Invalid Lat/Lon!", Toast.LENGTH_SHORT).show();
        mWeatherModel.resetLatLonError();
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