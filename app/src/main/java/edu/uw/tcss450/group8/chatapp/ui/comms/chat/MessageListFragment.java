package edu.uw.tcss450.group8.chatapp.ui.comms.chat;


import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentMessageListBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;

/**
 * Shows all the messages in a chat
 * Original code adapted from Charles Bryan
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @author Sean Logan
 * @author JenHo Liao
 * @version 5/19/22
 */
public class MessageListFragment extends Fragment {
    private MessageSendViewModel mSendModel;

    private MessageListViewModel mChatModel;
    private UserInfoViewModel mUserModel;

    private int chatid;
    private String chatName;

    public MessageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(MessageListViewModel.class);

        mSendModel = provider.get(MessageSendViewModel.class);
        chatid = MessageListFragmentArgs.fromBundle(getArguments()).getChatid();
        chatName = MessageListFragmentArgs.fromBundle(getArguments()).getChatName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());
        mChatModel.getFirstMessages(chatid, mUserModel.getJwt());

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        binding.swipeContainer.setRefreshing(true);

        final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.

        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.messageCard,typedValue,true);
        int color = typedValue.data;

        Log.e("chat id iew create in message frag", String.valueOf(chatid));
        rv.setAdapter(new MessageRecyclerViewAdapter(
                mChatModel.getMessageListByChatId(chatid),
                mUserModel.getEmail(),color));


        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatModel.getNextMessages(chatid, mUserModel.getJwt());
        });

        mChatModel.addMessageObserver(chatid, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(chatid,
                    mUserModel.getJwt().toString(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));
    }
}