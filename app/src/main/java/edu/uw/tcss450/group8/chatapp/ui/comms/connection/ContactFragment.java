package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

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
 * @version 5/31/22
 */
public class ContactFragment extends Fragment {

    private ContactListViewModel mContact;
    private UserInfoViewModel mUser;
    private FragmentContactBinding mBinding;
    private ContactRecyclerViewAdapter mAdapter;

    public ContactFragment() {

    }

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
        mContact.getContacts(mUser.getJwt());
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.addContactsListObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeContactsRefresh.setRefreshing(false);
            mAdapter = new ContactRecyclerViewAdapter(contacts, this);

            mBinding.listRoot.setAdapter(
                    mAdapter
            );
        });

        //refreshing chat list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContact.getContacts(mUser.getJwt());
            }
        });

        EditText editText = mBinding.searchBar;

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mAdapter != null) {
                    filter(editable.toString());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * filter list of contacts for search
     *
     * @param text String the string to check
     */
    private void filter(String text) {
        ArrayList<Contact> contactList = new ArrayList<>();
        for (Contact contact: mContact.getContactList()) {
            if(contact.getUserName().toLowerCase().contains(text.toLowerCase()) ||
                    contact.getEmail().toLowerCase().contains(text.toLowerCase())) {
                contactList.add(contact);
            }
        }
        mAdapter.contactList(contactList);
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