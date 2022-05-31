package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.add;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddCardBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.ContactFragment;

/**
 * Recycler View to show all contacts as a list.
 *
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/19/22
 */
public class ChatroomAddRecyclerViewAdapter extends RecyclerView.Adapter<ChatroomAddRecyclerViewAdapter.ChatroomAddViewHolder> {
    private final List<Contact> mContact;
    private final ChatroomAddFragment mParent;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    public ChatroomAddRecyclerViewAdapter(List<Contact> items, ChatroomAddFragment parent) {
        this.mContact= items;
        mParent = parent;
    }


    @NonNull
    @Override
    public ChatroomAddRecyclerViewAdapter.ChatroomAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomAddViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_add_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomAddRecyclerViewAdapter.ChatroomAddViewHolder holder, int position) {
        holder.setContact(mContact.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContact.size();
    }


    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class ChatroomAddViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatroomAddCardBinding mBinding;
        //public FragmentChatroomAddBinding mBinding2;

        public CheckBox mAdd;
        public Button messageFriend;

        public TextView email;
        public EditText name;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public ChatroomAddViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentChatroomAddCardBinding.bind(view);
            //mBinding2 = FragmentChatroomAddBinding.bind(view);
            mAdd = view.findViewById(R.id.checkBox_add);
            //messageFriend = view.findViewById(R.id.button_contact_send_message);
            //email = view.findViewById(R.id.text_contact_email);
            name = view.findViewById(R.id.edit_chatroom_add_name);


            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.namesToAdd.add(mBinding.textChatroomAddEmail.getText().toString());
                    Log.e("emailtoadd", mBinding.textChatroomAddEmail.getText().toString());
                }
            });

/*

            messageFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.sendMessage(email.getText().toString());
                }
            });

             */
        }



        /**
         * Set contact
         *
         * @param contact Contact the contact object
         */
        void setContact(final Contact contact) {
            mBinding.textChatroomAddUsername.setText(contact.getUserName());
            mBinding.textChatroomAddEmail.setText(contact.getEmail());
        }

    }
}
