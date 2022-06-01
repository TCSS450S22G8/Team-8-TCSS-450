package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.addUser;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddCardBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add.ChatroomAddFragment;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;

/**
 * Recycler View to show all contacts as a list in chatroom add.
 *
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @version 5/30/22
 */
/*
public class ChatroomAddUserRecyclerViewAdapter extends RecyclerView.Adapter<ChatroomAddUserRecyclerViewAdapter.ChatroomAddViewHolder> {
    private final List<Contact> mContact;
    private final ChatroomAddFragment mParent;

 */

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    /*
    public ChatroomAddUserRecyclerViewAdapter(List<Contact> items, ChatroomAddFragment parent) {
        this.mContact= items;
        mParent = parent;
    }


    @NonNull
    @Override
    public ChatroomAddUserRecyclerViewAdapter.ChatroomAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomAddViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_add_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomAddUserRecyclerViewAdapter.ChatroomAddViewHolder holder, int position) {
        holder.setContact(mContact.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContact.size();
    }
    */



    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    /*
    public class ChatroomAddViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatroomAddCardBinding mBinding;

        public CheckBox mAdd;

        public TextView email;
        public EditText name;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        /*
        public ChatroomAddViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentChatroomAddCardBinding.bind(view);
            mAdd = view.findViewById(R.id.checkBox_add);
            name = view.findViewById(R.id.edit_chatroom_add_name);


            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mParent.namesToAdd.contains(mBinding.textChatroomAddEmail.getText().toString())) {
                        mParent.namesToAdd.remove(mBinding.textChatroomAddEmail.getText().toString());
                        Log.e("emailList", mParent.namesToAdd.toString());
                    }
                    else{
                        mParent.namesToAdd.add(mBinding.textChatroomAddEmail.getText().toString());
                        Log.e("emailList", mParent.namesToAdd.toString());
                    }
                }
            });

        }
        */



        /**
         * Set contact
         *
         * @param contact Contact the contact object
         */
        /*
        void setContact(final Contact contact) {
            mBinding.textChatroomAddUsername.setText(contact.getUserName());
            mBinding.textChatroomAddEmail.setText(contact.getEmail());
        }

    }
}
*/
