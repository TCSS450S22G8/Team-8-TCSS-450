package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomInfoBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;

/**
 * Create an instance of Chatroom Info fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Levi McCoy
 * @version 6/5/22
 */

public class ChatroomInfoFragment extends Fragment{

    private ChatroomInfoListViewModel mAdd;
    private UserInfoViewModel mUser;
    private FragmentChatroomInfoBinding mBinding;
    private ContactListViewModel mContact;
    private ChatroomViewModel mView;
    private int chatId;
    List<String> namesToAdd = new ArrayList<String>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdd = new ViewModelProvider(getActivity()).get(ChatroomInfoListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mView = new ViewModelProvider(getActivity()).get(ChatroomViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatroom_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentChatroomInfoBinding.bind(getView());
        mView.addChatIdObserver(getViewLifecycleOwner(), getId -> {
            mBinding.listRoot.setVisibility(View.GONE);
            mBinding.progressBar.setVisibility(View.VISIBLE);
            chatId = getId;
            mAdd.getContacts(mUser.getJwt(),chatId);
        });
        mAdd.addGetContactsObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeChatroomInfoRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ChatroomInfoRecyclerViewAdapter(contacts, this)
            );
        });

        mBinding.swipeChatroomInfoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdd.getContacts(mUser.getJwt(),chatId);
            }
        });

    }





}


