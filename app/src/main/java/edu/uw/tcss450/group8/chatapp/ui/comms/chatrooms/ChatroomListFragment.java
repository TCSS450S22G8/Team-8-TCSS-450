package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import android.app.ActionBar;
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

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomListBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.auth.login.LoginFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.MessageListViewModel;


/**
 * Create an instance of Message List fragment
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/19/22
 */
public class ChatroomListFragment extends Fragment {
    private ChatroomViewModel mModel;

    private UserInfoViewModel mUser;

    private MessageListViewModel mMessage;

    private FragmentChatroomListBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider viewModelProvider = new ViewModelProvider(getActivity());
        mModel = viewModelProvider.get(ChatroomViewModel.class);
        mUser = viewModelProvider.get(UserInfoViewModel.class);
        mMessage = viewModelProvider.get(MessageListViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatroom_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentChatroomListBinding.bind(getView());

        super.onViewCreated(view, savedInstanceState);
        mModel.addChatRoomListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {
                chatList.forEach(chatroom -> {
                    int chatId = Integer.parseInt(chatroom.getChatRoomId());
                    mMessage.getFirstMessages(chatId, mUser.getJwt());
                    mMessage.addMessageObserver(chatId, getViewLifecycleOwner(), messages -> {
                        mBinding.listRoot.setAdapter(
                                new ChatroomRecyclerViewAdapter(chatList, this)
                        );
                    });
                });
                mBinding.swipeContactsRefresh.setRefreshing(false);
            }
        });

        //refreshing friend list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mModel.getChatRoomsForUser(mUser.getJwt());
            }
        });

        mBinding.floatingButtonAdd.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ChatroomListFragmentDirections.actionNavChatroomFragmentToChatroomAddFragment()
                ));
    }

    /**
     * Enters a chat room with a contact.
     *
     * @param chatId int
     */
    public void startChat(int chatId, String chatName) {
        Navigation.findNavController(getView()).
                navigate(ChatroomListFragmentDirections.
                        actionNavChatroomFragmentToMessageListFragment(chatName, chatId));

    }


}
