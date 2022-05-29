package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.auth.login.LoginFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactRecyclerViewAdapter;

/**
 * Create an instance of Contact List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/19/22
 */
public class ChatroomAddFragment extends Fragment{

    private ChatroomAddListViewModel mContact;
    private UserInfoViewModel mUser;
    private FragmentChatroomAddBinding mBinding;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = new ViewModelProvider(getActivity()).get(ChatroomAddListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatroom_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentChatroomAddBinding.bind(getView());


        mBinding.buttonAddChat.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        // get user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.addChatroomAddListObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeChatroomAddRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ChatroomAddRecyclerViewAdapter(contacts, this)
            );
        });

        //refreshing chat list swipe
        mBinding.swipeChatroomAddRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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