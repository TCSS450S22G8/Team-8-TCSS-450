package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * top level fragment for contacts. contains a pageview of friends, adding friends, and viewing
 * requests
 *
 * @author shilnara dam
 * @author rin pham
 * @version 5/31/22
 */
public class ContactTabFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private ContactTabsAdapter mAdapter;

    /**
     * Required empty constructor
     */
    public ContactTabFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_tab, container, false);
    }


    @Override
    public void onViewCreated(@NonNull final View theView,
                              @Nullable final Bundle theSavedInstanceState) {
        super.onViewCreated(theView, theSavedInstanceState);
        List<Fragment> contactFragments = new ArrayList<>();
        contactFragments.add(new ContactFragment());
        contactFragments.add(new AddContactFragment());
        contactFragments.add(new ContactRequestFragment());

        mTabLayout = getActivity().findViewById(R.id.contact_tabs);
        mViewPager2 = getActivity().findViewById(R.id.contact_viewpage);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new ContactTabsAdapter(fragmentManager, getActivity().getLifecycle(), contactFragments);
        if (mViewPager2 != null ) {
            mViewPager2.setAdapter(mAdapter);
            new TabLayoutMediator(mTabLayout, mViewPager2, (tab, position) -> {
                switch (position) {
                    case 0:
                        tab.setText("All friends");
                        break;
                    case 1:
                        tab.setText("Add friends");
                        break;
                    case 2:
                        tab.setText("view Requests");
                        break;
                }
            }).attach();
        }



    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}