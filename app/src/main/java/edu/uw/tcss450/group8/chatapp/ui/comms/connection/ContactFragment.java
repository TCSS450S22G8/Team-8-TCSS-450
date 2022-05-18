package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * Create an instance of Contact List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/17/22
 */
public class ContactFragment extends Fragment{
    private ContactListViewModel mContact;
    private UserInfoViewModel mUser;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactBinding binding = FragmentContactBinding.bind(getView());

        // get user contacts
        //mContact.getContacts(mUser.getJwt());
        mContact.getContacts("4");
        mContact.addContactsListObserver(getViewLifecycleOwner(), contacts -> {
                binding.listRoot.setAdapter(
                        new ContactRecyclerViewAdapter(contacts, this)
                );
        });

    }

    /**
     * This method will unfriend(delete) a contact in the contact list.
     * @param email the email of the contact that will be deleted
     */
    public void unFriend(String email) {

        mContact.unfriend(mUser.getJwt(), email);
    }

    /**
     * This method will send a message to a contact in the contact list.
     * @param email the email of the contact that will be sent a message
     */
    public void sendMessage(String email) {
        Bundle bundle = new Bundle();
        bundle.putString("email", email);
       // Navigation.findNavController(getView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToVerifyFragment());
    }
}