package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

/**
 * tab adapter for 3 tab contacts page
 *
 * @author shilnara dam
 * @author rin pham
 * @version 5/31/22
 */
public class ContactTabsAdapter extends FragmentStateAdapter {

    private List<Fragment> mFragments;


    /**
     * constructor for contact labs adaptor
     *
     * @param fragmentManager FragmentManager the fragment manager
     * @param lifecycle Lifecycle the lifecycle
     * @param theFrags List<Fragment> the list of contact fragments
     */
    public ContactTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> theFrags) {
        super(fragmentManager, lifecycle);
        mFragments = theFrags;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }



}