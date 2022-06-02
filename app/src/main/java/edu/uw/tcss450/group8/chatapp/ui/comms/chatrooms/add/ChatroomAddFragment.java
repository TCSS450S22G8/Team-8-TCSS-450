package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add;

import android.app.AlertDialog;
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
 * @version 6/2/22
 */
public class ChatroomAddFragment extends Fragment{

    private ChatroomAddListViewModel mAdd;
    private UserInfoViewModel mUser;
    private FragmentChatroomAddBinding mBinding;
    private ContactListViewModel mContact;
    private ChatroomViewModel mForChats;
    List<String> namesToAdd = new ArrayList<String>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdd = new ViewModelProvider(getActivity()).get(ChatroomAddListViewModel.class);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mContact = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mForChats = new ViewModelProvider(getActivity()).get(ChatroomViewModel.class);
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

        mBinding.buttonAddChat.setOnClickListener(this::attemptAdd);

        // get user contacts
        mBinding.progressBar.setVisibility(View.GONE);
        mBinding.swipeChatroomAddRefresh.setRefreshing(false);
        mBinding.listRoot.setAdapter(
                new ChatroomAddRecyclerViewAdapter(mContact.getContacts(), this)
        );

        // get user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.addContactsListObserver(getViewLifecycleOwner(), contacts -> {
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
     * attempt to start adding process
     *
     * @param view current view
     */
    public void attemptAdd(View view) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e("JWT", mUser.getJwt());
        Log.e("mynames", namesToAdd.toString() );
        if (mBinding.editChatroomAddName.getText().toString().trim().equals("")) {
//            mBinding.editChatroomAddName.setError("Must have a name for the chat room!");
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Make sure you are naming your chat room!")
                    .setNegativeButton("Okay", null)
                    .show().setCanceledOnTouchOutside(true);
            mBinding.progressBar.setVisibility(View.GONE);
            return;
        }
        else {
            mAdd.add1(mUser.getJwt(), mBinding.editChatroomAddName.getText().toString().trim(), namesToAdd, mUser.getEmail(), mForChats.getChatRooms(), mForChats.getChatRoomsList());
            mBinding.progressBar.setVisibility(View.GONE);
            Navigation.findNavController(getView()).navigate(
                    ChatroomAddFragmentDirections.actionChatroomAddFragmentToNavChatroomFragment());
        }
    }

}