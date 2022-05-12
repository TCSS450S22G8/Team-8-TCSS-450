package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherCardBinding;

public class WeatherDailyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherDailyRecyclerViewAdapter.WeatherDailyViewHolder> {

    private final List<Weather> mWeather;

    public WeatherDailyRecyclerViewAdapter(List<Weather> theItems) {
        this.mWeather = theItems;

    }

    @NonNull
    @Override
    public WeatherDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherDailyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherDailyViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeather.size();
    }

    public class WeatherDailyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherCardBinding mBinding;
        private Weather mWeather;

        public WeatherDailyViewHolder(@NonNull View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentWeatherCardBinding.bind(theView);
        }

        /**
         * populating daily weather with time, temperature, and icons/image
         *
         * @param theWeather Weather the weather forecast
         */
        void setWeather(final Weather theWeather) {
            mWeather = theWeather;
            String[] date = theWeather.getTime().split(" ");
            mBinding.textTime.setText(date[0]);
            mBinding.textTemperature.setText((theWeather.getTemp()));

            //switch statement to set icons
            // https://stackoverflow.com/questions/45673935/how-to-get-icon-from-openweathermap
        }
    }
}
