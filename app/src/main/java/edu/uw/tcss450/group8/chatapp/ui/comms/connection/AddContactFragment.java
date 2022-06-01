package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactAddContactBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * fragment to request to add new users as friends.
 *
 * @author shilnara dam
 * @author rin pham
 * @version 5/31/22
 */
public class AddContactFragment extends Fragment {

    private ContactListViewModel mContact;
    private UserInfoViewModel mUser;
    private FragmentContactAddContactBinding mBinding;
    private AddContactRecyclerViewAdapter mAdapter;

    public AddContactFragment() {

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentContactAddContactBinding.bind(getView());

        // get non user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.getNonFriendList(mUser.getJwt());
        mContact.addNonContactsListObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeContactsRefresh.setRefreshing(false);
            mAdapter = new AddContactRecyclerViewAdapter(contacts, this);

            mBinding.listRoot.setAdapter(
                    mAdapter
            );
        });

        //adding button listeners for navigation
        mBinding.buttonContactsContacts.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    AddContactFragmentDirections
                            .actionAddContactFragmentToNavConnectionsFragment());
        });

        mBinding.buttonContactsRequests.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    AddContactFragmentDirections
                            .actionAddContactFragmentToContactRequestFragment());
        });

        //refreshing chat list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContact.getNonFriendList(mUser.getJwt());
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
     * filters through list of non friends by regex
     * @param text
     */
    private void filter(String text) {
        ArrayList<Contact> contactList = new ArrayList<>();
        for (Contact contact: mContact.getNonContactList()) {
            if(contact.getUserName().toLowerCase().contains(text.toLowerCase()) ||
                    contact.getEmail().toLowerCase().contains(text.toLowerCase())) {
                contactList.add(contact);
            }
        }
        mAdapter.contactList(contactList);

    }

    /**
     * calls view model to send request to add a user as a friend
     * @param email String the user's email
     */
    public void addFriend(String email) {
        mContact.addFriend(mUser.getJwt(), email);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_add_contact, container, false);
    }
}