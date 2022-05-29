package edu.uw.tcss450.group8.chatapp.ui.location;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLocationCardBinding;

/**
 * Adapter for recycler view
 * will display all user saved locations
 *
 * @author shilnara dam
 * @version 5/28/22
 */
public class LocationListRecyclerViewAdapter extends RecyclerView.Adapter<LocationListRecyclerViewAdapter.LocationListViewHolder> {

    private final List<Location> mLocations;
    private final LocationListFragment mParent;

    /**
     * instantiate the recycler view
     *
     * @param theItems List<Location> location items
     * @param theParent LocationListFragment the parent fragment
     */
    public LocationListRecyclerViewAdapter(List<Location> theItems, LocationListFragment theParent) {
        this.mLocations = theItems;
        mParent = theParent;
    }

    @NonNull
    @Override
    public LocationListRecyclerViewAdapter.LocationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocationListRecyclerViewAdapter.LocationListViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_location_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LocationListRecyclerViewAdapter.LocationListViewHolder holder, int position) {
        holder.setLocation(mLocations.get(position));
    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    /**
     * inner class for view holder
     * represents individual cards
     */
    public class LocationListViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private final FragmentLocationCardBinding mBinding;
        private final ImageButton mDelete;
        private Location mSingleLocation;

        /**
         * instantiate the view holder
         *
         * @param theView current view
         */
        public LocationListViewHolder(@NonNull View theView) {
            super(theView);
            mView = theView;
            mBinding = FragmentLocationCardBinding.bind(theView);
            mDelete = theView.findViewById(R.id.button_location_remove);


            //delete saved location
            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.deleteLocation(mSingleLocation.getLat(), mSingleLocation.getLon());
                    mLocations.remove((getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mLocations.size());
                    Toast.makeText(mParent.getActivity(), "Removed Location", Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * populating location names
         *
         * @param theLocation Location the location
         */
        void setLocation(final Location theLocation) {
            mSingleLocation = theLocation;
            mBinding.textLocationCityCountry.setText(theLocation.getCity());
        }
    }

}

