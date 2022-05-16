package edu.uw.tcss450.group8.chatapp.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherCardBinding;

/**
 * Adapter for recycler view
 * will display the hourly weather forecast for a location
 *
 * @author shilnara dam
 * @version 5/14/22
 */
public class WeatherHourlyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherHourlyRecyclerViewAdapter.WeatherHourlyViewHolder> {

    private final List<Weather> mWeather;

    /**
     * instantiate the recycler view
     * @param theItems List<Weather> weather items
     */
    public WeatherHourlyRecyclerViewAdapter(List<Weather> theItems) {
        this.mWeather = theItems;
    }

    @NonNull
    @Override
    public WeatherHourlyRecyclerViewAdapter.WeatherHourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHourlyViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHourlyRecyclerViewAdapter.WeatherHourlyViewHolder holder, int position) {
        holder.setWeather(mWeather.get(position));
    }

    @Override
    public int getItemCount() {
        return mWeather.size();
    }

    /**
     * inner class for view holder
     */
    public class WeatherHourlyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherCardBinding mBinding;
        private Weather mWeather;

        /**
         * instantiate the view holder
         *
         * @param theView current view
         */
        public WeatherHourlyViewHolder(@NonNull View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentWeatherCardBinding.bind(theView);
        }

        /**
         * populating hourly weather with time, temperature, and icons/image
         *
         * @param theWeather Weather the weather forecast
         */
        void setWeather(final Weather theWeather) {
            mWeather = theWeather;
            Picasso.get().load(mWeather.getIcon()).into(mBinding.imageWeather);
            mBinding.textTemperature.setText(mWeather.getTemp());
            mBinding.textTime.setText(mWeather.getTime());
        }
    }



}
