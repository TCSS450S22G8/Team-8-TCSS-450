package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddCardBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.auth.login.LoginFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomListFragmentDirections;
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

    private ChatroomAddListViewModel mAdd;
    private UserInfoViewModel mUser;
    private FragmentChatroomAddBinding mBinding;
    private FragmentChatroomAddCardBinding mBinding2;
    List<String> namesToAdd = new ArrayList<String>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdd = new ViewModelProvider(getActivity()).get(ChatroomAddListViewModel.class);
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


       // mBinding.buttonAddChat.setOnClickListener(button ->
                //Navigation.findNavController(getView()).navigate(
                        //ChatroomAddFragmentDirections.actionChatroomAddFragmentToNavChatroomFragment()
                //unFriend()
                //);

        mBinding.buttonAddChat.setOnClickListener(this::attemptAdd);

        // get user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mAdd.addChatroomAddListObserver(getViewLifecycleOwner(), contacts -> {
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
                mAdd.getContacts(mUser.getJwt());
            }
        });

    }


    /**
     * unfriend a contact
     *
     *
     */
    public void attemptAdd(View view) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e("JWT", mUser.getJwt());
        Log.e("mynames", namesToAdd.toString() );
        //mBinding2.checkBoxAdd.isChecked();
        mAdd.add1(mUser.getJwt(), mBinding.editChatroomAddName.getText().toString().trim(),namesToAdd);
        mBinding.progressBar.setVisibility(View.GONE);
        Navigation.findNavController(getView()).navigate(
        ChatroomAddFragmentDirections.actionChatroomAddFragmentToNavChatroomFragment());
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