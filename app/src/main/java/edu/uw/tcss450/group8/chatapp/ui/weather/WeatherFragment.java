package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.squareup.picasso.Picasso;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherBinding;

/**
 * Class for Weather Fragment to display weather
 *
 *  @author Shilnara Dam
 *  @version 5/13/22
 */
public class WeatherFragment extends Fragment {
    private WeatherViewModel mWeatherModel;



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

        FragmentWeatherBinding mBinding = FragmentWeatherBinding.bind(getView());
         //   mBinding.textWeatherCity.setText(string);
        //});
        mWeatherModel.addCurrentWeatherObserver(getViewLifecycleOwner(), weather -> {
            mBinding.textWeatherCity.setText(weather.getCity());
            Picasso.get().load(weather.getIcon()).into(mBinding.imageWeatherCondition);
            mBinding.textWeatherCurrentTemp.setText(weather.getTemp());
            mBinding.textWeatherCondition.setText(weather.getCondition());
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}