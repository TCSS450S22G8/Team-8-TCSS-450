package edu.uw.tcss450.group8.chatapp.ui.location;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLocationMapBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * Fragment to display map for user to view and add locations
 *
 * @author shilnara dam
 * @version 5/30/22
 */
public class LocationMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private LocationViewModel mModel;
    private LocationListViewModel mListModel;
    private UserInfoViewModel mUser;
    private GoogleMap mMap;
    private Marker marker;
    private FragmentLocationMapBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListModel = new ViewModelProvider(getActivity()).get(LocationListViewModel.class);
        mModel = new ViewModelProvider(getActivity()).get(LocationViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentLocationMapBinding binding = FragmentLocationMapBinding.bind(getView());
        mModel = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        //mModel.addLocationObserver(getViewLifecycleOwner(), location ->
        //binding.textLatLong.setText(location.toString()));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //add this fragment as the OnMapReadyCallback -> See onMapReady()
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //allows for searching locations
        Log.e("TAG", "onViewCreated: 1");
        mBinding = FragmentLocationMapBinding.bind(requireView());
        mBinding.searchLocationMap.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = mBinding.searchLocationMap.getQuery().toString();
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(getActivity());
                //using try catch to check if text input is valid
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                } catch (IOException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        UiSettings settings = mMap.getUiSettings();
        settings.setZoomControlsEnabled(true);
        settings.setCompassEnabled(true);
        LocationViewModel model = new ViewModelProvider(getActivity())
                .get(LocationViewModel.class);
        model.addLocationObserver(getViewLifecycleOwner(), location -> {
            if (location != null) {
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                final LatLng c = new LatLng(location.getLatitude(), location.getLongitude());
                //Zoom levels are from 2.0f (zoomed out) to 21.f (zoomed in)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15.0f));
            }
        });
        mMap.setOnMapClickListener(this);

    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        if (marker!=null) {
            marker.remove();
        }
        marker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("New Marker"));
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Do you want to add this location?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListModel.addLocation(mUser.getJwt(), latLng.latitude, latLng.longitude);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show().setCanceledOnTouchOutside(true);
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                        latLng, mMap.getCameraPosition().zoom));
    }

    public void observeError(@NonNull LatLng latLng) {

    }

    public void observeSuccess(@NonNull LatLng latLng) {

    }
}