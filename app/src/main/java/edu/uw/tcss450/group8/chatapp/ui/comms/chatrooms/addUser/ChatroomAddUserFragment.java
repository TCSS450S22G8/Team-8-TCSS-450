package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser;

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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddUserBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.ChatroomViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser.ChatroomAddUserFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser.ChatroomAddUserListViewModel;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser.ChatroomAddUserRecyclerViewAdapter;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactListViewModel;

/**
 * Create an instance of Chatroom Add List fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Levi McCoy
 * @version 5/30/22
 */

public class ChatroomAddUserFragment extends Fragment{

    private ChatroomAddUserListViewModel mAdd;
    private UserInfoViewModel mUser;
    private FragmentChatroomAddUserBinding mBinding;
    private ContactListViewModel mContact;
    private ChatroomViewModel mView;
    private ChatroomRecyclerViewAdapter mView2;
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

        // get user contacts
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeChatroomAddUserRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ChatroomAddUserRecyclerViewAdapter(mContact.getContacts(), this)
            );

        // get user contacts
        mBinding.listRoot.setVisibility(View.GONE);
        mBinding.progressBar.setVisibility(View.VISIBLE);
        mContact.addContactsListObserver(getViewLifecycleOwner(), contacts -> {
            mBinding.listRoot.setVisibility(View.VISIBLE);
            mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swipeChatroomAddUserRefresh.setRefreshing(false);
            mBinding.listRoot.setAdapter(
                    new ChatroomAddUserRecyclerViewAdapter(contacts, this)
            );
        });

        //refreshing chat list swipe
        mBinding.swipeChatroomAddUserRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mContact.getContacts(mUser.getJwt());
            }
        });

    }





    public void attemptAdd(View view) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
        Log.e("JWT", mUser.getJwt());
        Log.e("mynames", namesToAdd.toString() );
        //if (mBinding.editChatroomAddUserName.getText().toString().trim().equals("")) {
//            //mBinding.editChatroomAddUserName.setError("Must have a name for the chat room!");
            //AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            //dialog.setTitle("Make sure you are naming your chat room!")
                    //.setNegativeButton("Okay", null)
                   // .show().setCanceledOnTouchOutside(true);
            //mBinding.progressBar.setVisibility(View.GONE);
           // return;
       // }
        //else {
            //Bundle bundle = this.getArguments();
            //int myInt = bundle.getInt("chatId");
        //int myInt = mView.ch;
       // Log.e("THEBUNDLENUM", ": "+myInt );
        Log.e("THENUM", ": "+mView.getmChatId().getValue());
            chatId = mView.getmChatId().getValue();
            Log.e("THENUM2", ": "+chatId);
            mAdd.add1(mUser.getJwt(), namesToAdd, chatId);
            mBinding.progressBar.setVisibility(View.GONE);
            Navigation.findNavController(getView()).navigate(
                    ChatroomAddUserFragmentDirections.actionChatroomAddUserFragmentToNavChatroomFragment());
        //}
    }

}


