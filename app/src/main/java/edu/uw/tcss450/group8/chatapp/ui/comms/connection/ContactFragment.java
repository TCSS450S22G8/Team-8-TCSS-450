package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * Create an instance of Contact List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/19/22
 */
public class ContactFragment extends Fragment{

    private ContactListViewModel mContact;
    private UserInfoViewModel mUser;
    private FragmentContactBinding mBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentContactBinding.bind(getView());

        // get user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.getContacts(mUser.getJwt());
        mContact.addContactsListObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeContactsRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contacts, this)
            );
        });

        //removing friend observer to makes toast message
        mContact.addUnFriendObserver(getViewLifecycleOwner(), isUnfriend -> {
            Toast.makeText(getActivity(), "Unfriend success!", Toast.LENGTH_SHORT).show();
        });

        //refreshing chat list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContact.getContacts(mUser.getJwt());
            }
        });

    }

    /**
     * unfriend a contact
     *
     * @param email String email of the friend
     */
    public void unFriend(String email) {
        mContact.unfriend(mUser.getJwt(), email);
    }

    /**
     * open chatroom with the desired contact
     *
     * @param email String email of the friend
     */
    public void sendMessage(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
    }
}