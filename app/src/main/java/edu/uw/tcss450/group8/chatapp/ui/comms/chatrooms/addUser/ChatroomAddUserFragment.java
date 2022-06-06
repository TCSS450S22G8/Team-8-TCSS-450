package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddUserBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;

/**
 * Create an instance of Chatroom Add List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Levi McCoy
 * @version 6/5/22
 */

public class ChatroomAddUserFragment extends Fragment{

    private ChatroomAddUserListViewModel mAdd;
    private UserInfoViewModel mUser;
    private FragmentChatroomAddUserBinding mBinding;
    private ContactListViewModel mContact;
    private ChatroomViewModel mView;
    private int chatId;
    List<String> namesToAdd = new ArrayList<String>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdd = new ViewModelProvider(getActivity()).get(ChatroomAddUserListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mView = new ViewModelProvider(getActivity()).get(ChatroomViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatroom_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentChatroomAddUserBinding.bind(getView());


        mBinding.buttonAdduserChat.setOnClickListener(this::attemptAdd);
        mView.addChatIdObserver(getViewLifecycleOwner(), getId -> {
            mBinding.listRoot.setVisibility(View.GONE);
            mBinding.progressBar.setVisibility(View.VISIBLE);
            chatId = getId;
            mAdd.getContactsNot(mUser.getJwt(),chatId);
        });
        mAdd.addGetContactsNotObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeChatroomAddUserRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ChatroomAddUserRecyclerViewAdapter(contacts, this)
            );
        });

        mBinding.swipeChatroomAddUserRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdd.getContactsNot(mUser.getJwt(),chatId);
            }
        });

    }




    /**
     * attempt to start adding users process
     *
     * @param view current view
     */
    public void attemptAdd(View view) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        chatId = mView.getmChatId().getValue();
        mAdd.add1(mUser.getJwt(), namesToAdd, chatId);
        mBinding.progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Added User(s)", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getView()).navigate(
                ChatroomAddUserFragmentDirections.actionChatroomAddUserFragmentToNavChatroomFragment());
    }

}


