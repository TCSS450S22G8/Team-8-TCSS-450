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

import java.util.Objects;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomListBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;


/**
 * Create an instance of Message List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 1.0
 */
public class ChatroomListFragment extends Fragment {
    private ChatroomViewModel mModel;

    private UserInfoViewModel mUser;

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
        super.onViewCreated(view, savedInstanceState);
        FragmentChatroomListBinding binding = FragmentChatroomListBinding.bind(getView());
        mModel.getChatRoomsForUser(mUser.getJwt());
        mModel.addChatRoomListObserver(getViewLifecycleOwner(), chatList -> {
            if (!chatList.isEmpty()) {

                binding.listRoot.setAdapter(
                        new ChatroomRecyclerViewAdapter(chatList, this)
                );
//                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }

    public void startChat(int chatId) {
        Navigation.findNavController(getView()).
                navigate(ChatroomListFragmentDirections.
                        actionNavChatroomFragmentToMessageListFragment(chatId));
    }
}
