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
 * will display the daily weather forecast for a location
 *
 * @author shilnara dam
 * @version 5/14/22
 */
public class WeatherDailyRecyclerViewAdapter extends RecyclerView.Adapter<WeatherDailyRecyclerViewAdapter.WeatherDailyViewHolder> {

    private final List<Weather> mWeather;

    /**
     * instantiate the recycler view
     * @param theItems List<Weather> weather items
     */
    public WeatherDailyRecyclerViewAdapter(List<Weather> theItems) {
        this.mWeather = theItems;
    }

    @NonNull
    @Override
    public WeatherDailyRecyclerViewAdapter.WeatherDailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    /**
     * inner class for view holder
     */
    public static class WeatherDailyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentWeatherCardBinding mBinding;

        /**
         * instantiate the view holder
         *
         * @param theView current view
         */
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
            Picasso.get().load(theWeather.getIcon()).into(mBinding.imageWeather);
            mBinding.textTemperature.setText(theWeather.getTemp());
            mBinding.textTime.setText(theWeather.getTime());
        }
    }
}
