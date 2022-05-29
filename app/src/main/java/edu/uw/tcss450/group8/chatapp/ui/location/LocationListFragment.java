package edu.uw.tcss450.group8.chatapp.ui.location;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLocationBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherDailyRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherHourlyRecyclerViewAdapter;

/**
 * Shows a list of locations the user would like to view weather information for
 * user can add and delete locations
 *
 * @author shilnara dam
 * @version 5/29/22
 */
public class LocationListFragment extends Fragment {

    private FragmentLocationBinding mBinding;
    private LocationListViewModel mModel;
    private UserInfoViewModel mUser;


    public LocationListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(requireActivity()).get(LocationListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentLocationBinding.bind(requireView());

        //get all saved locations
        mModel.getLocations(mUser.getJwt());

        mModel.addLocationsObserver(getViewLifecycleOwner(), locations -> {
            mBinding.swipeContactsRefresh.setRefreshing(false);
            mBinding.listLocations.setVisibility(View.VISIBLE);
            //mBinding.progressBar.setVisibility(View.GONE);
            //mBinding.swipeContactsRefresh.setRefreshing(false);
            mBinding.listLocations.setAdapter(
                    new LocationListRecyclerViewAdapter(locations, this)
            );
        });


        //refreshing chat list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mModel.getLocations(mUser.getJwt());
            }
        });

        //button listener for moving to map
        mBinding.buttonOpenMap.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    LocationListFragmentDirections
                            .actionLocationFragmentToLocationMapFragment());
        });
    }

    public void deleteLocation(String theLat, String theLon) {
        mModel.deleteLocation(mUser.getJwt(), theLat, theLon);
    }
}