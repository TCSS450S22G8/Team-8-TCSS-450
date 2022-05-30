package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

/**

 * create an instance of this fragment.
 */
public class ContactTabsAdapter extends FragmentStateAdapter {

    private String[] titles = new String[]{"All Contacts", "Add contact", "Request"};



    public ContactTabsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0:
                return new ContactFragment();
            case 1:
                return new AddContactFragment();
            case 2:
                return new ContactRequestFragment();
        }
        return new ContactFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }



}