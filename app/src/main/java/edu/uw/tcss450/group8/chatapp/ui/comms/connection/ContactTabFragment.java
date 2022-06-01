package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import edu.uw.tcss450.group8.chatapp.R;

/**

 * create an instance of this fragment.
 */
public class ContactTabFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager2;
    private ContactTabsAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragment_contact_tab);


        mTabLayout = getActivity().findViewById(R.id.contact_tabs);
        mViewPager2 = getActivity().findViewById(R.id.contact_viewpage);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mAdapter = new ContactTabsAdapter(fragmentManager, getActivity().getLifecycle());
        mViewPager2.setAdapter(mAdapter);




        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_tab, container, false);
    }
}