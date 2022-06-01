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

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactRequestBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * create an instance of this fragment.
 */
public class ContactRequestFragment extends Fragment {

    private ContactListViewModel mContact;
    private UserInfoViewModel mUser;
    private FragmentContactRequestBinding mBinding;
    private ContactIncomingRecyclerViewAdapter mAdapterIncoming;
    private ContactOutgoingRecyclerViewAdapter mAdapterOutgoing;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentContactRequestBinding.bind(getView());

        // get incoming and outgoing requests
        mBinding.listContactIncoming.setVisibility(View.GONE);
        mBinding.listContactOutgoing.setVisibility(View.GONE);

        mContact.getIncomingRequestList(mUser.getJwt());
        mContact.incomingRequestListObserver(getViewLifecycleOwner(), contacts -> {
            Log.e("incoming", "inside incoming recycler");
            mBinding.listContactIncoming.setVisibility(View.VISIBLE);
            mAdapterIncoming = new ContactIncomingRecyclerViewAdapter(contacts, this);
            mBinding.listContactIncoming.setAdapter(
                    mAdapterIncoming
            );

        });
        mContact.getOutgoingRequestList(mUser.getJwt());
        mContact.outgoingRequestListObserver(getViewLifecycleOwner(), contacts -> {
            Log.e("outgoing", "inside outgoing recycler");
            mBinding.listContactOutgoing.setVisibility(View.VISIBLE);
            mAdapterOutgoing = new ContactOutgoingRecyclerViewAdapter(contacts, this);
            mBinding.listContactOutgoing.setAdapter(
                    mAdapterOutgoing
            );

        });


    }

    /**
     * Accept a friend request
     *
     * @param email String email of the friend
     */
    public void acceptFriendRequest(String email) {
        mContact.acceptFriendRequest(mUser.getJwt(), email);
    }


    /**
     * Delete a friend request
     *
     * @param email String email of the friend
     */
    public void deleteFriendRequest(String email) {
        mContact.deleteFriendRequest(mUser.getJwt(), email);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_request, container, false);
    }
}