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
 *  @version 5/14/22
 */
public class WeatherFragment extends Fragment {
    private WeatherViewModel mWeatherModel;
    private FragmentWeatherBinding mBinding;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWeatherModel = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);

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

        mBinding = FragmentWeatherBinding.bind(getView());
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mWeatherModel.addCurrentWeatherObserver(
                getViewLifecycleOwner(),
                this::observeCurrentWeatherResponse);

        mBinding.buttonWeatherZipcodeEnter.setOnClickListener(button -> {
            try {
                //remove text focus
                mBinding.editWeatherZipcode.clearFocus();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard not closed");
            }
            setDataToBlank();
            mWeatherModel.getWeatherZipCode(mBinding.editWeatherZipcode.getText().toString());
        });

        mBinding.buttonWeatherLatLonEnter.setOnClickListener(button -> {
            try {
                //remove text focus
                mBinding.editWeatherLat.clearFocus();
                mBinding.editWeatherLon.clearFocus();
                //close keyboard
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            } catch (Exception e) {
                Log.i("ANDROID", "Keyboard not closed");
            }
            setDataToBlank();
            mWeatherModel.getWeatherLatLon(mBinding.editWeatherLat.getText().toString(), mBinding.editWeatherLon.getText().toString());
        });

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param theWeather the Response from the server
     */
    private void observeCurrentWeatherResponse(final Weather theWeather) {
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.imageWeatherCondition.setVisibility(View.VISIBLE);
        mBinding.textWeatherCity.setText(theWeather.getCity());
        Picasso.get().load(theWeather.getIcon()).into(mBinding.imageWeatherCondition);
        mBinding.textWeatherCurrentTemp.setText(theWeather.getTemp());
        mBinding.textWeatherCondition.setText(theWeather.getCondition());
    }

    /**
     * when loading weather info, all current data should be set to blank
     */
    private void setDataToBlank() {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mBinding.textWeatherCity.setText("");
        mBinding.imageWeatherCondition.setVisibility(View.INVISIBLE);
        mBinding.textWeatherCurrentTemp.setText("");
        mBinding.textWeatherCondition.setText("");
    }
}