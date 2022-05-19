package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomListBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;


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

    private FragmentChatroomListBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ChatroomViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

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
        mModel.getChatRoomsForUser(mUser.getJwt());
        mModel.addChatRoomListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {
                mBinding.swipeContactsRefresh.setRefreshing(false);
                mBinding.listRoot.setAdapter(
                        new ChatroomRecyclerViewAdapter(chatList, this)
                );
            }
        });

        //refreshing friend list swipe
        mBinding.swipeContactsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mModel.getChatRoomsForUser(mUser.getJwt());
            }
        });
    }

    /**
     * Enters a chat room with a contact.
     *
     * @param chatId int
     */
    public void startChat(int chatId) {
        Navigation.findNavController(getView()).
                navigate(ChatroomListFragmentDirections.
                        actionNavChatroomFragmentToMessageListFragment(chatId));
    }
}
