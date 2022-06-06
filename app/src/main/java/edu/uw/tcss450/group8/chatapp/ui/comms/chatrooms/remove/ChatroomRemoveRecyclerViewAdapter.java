package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms.remove;

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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomRemoveCardBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;

/**
 * Recycler View to show all contacts as a list in chatroom remove.
 *
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @author Levi McCoy
 * @version 6/5/22
 */

public class ChatroomRemoveRecyclerViewAdapter extends RecyclerView.Adapter<ChatroomRemoveRecyclerViewAdapter.ChatroomRemoveViewHolder> {
    private final List<Contact> mContact;
    private final ChatroomRemoveFragment mParent;



    /**
     * Constructor for RemoveRecyclerViewAdapter
     *
     * @param items list of message
     */
    public ChatroomRemoveRecyclerViewAdapter(List<Contact> items, ChatroomRemoveFragment parent) {
        this.mContact= items;
        mParent = parent;
    }


    @NonNull
    @Override
    public ChatroomRemoveRecyclerViewAdapter.ChatroomRemoveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomRemoveViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_remove_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomRemoveRecyclerViewAdapter.ChatroomRemoveViewHolder holder, int position) {
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
    public class ChatroomRemoveViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatroomRemoveCardBinding mBinding;

        public CheckBox mAdd;

        public TextView email;
        public EditText name;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public ChatroomRemoveViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentChatroomRemoveCardBinding.bind(view);
            mAdd = view.findViewById(R.id.checkBox_remove);

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mParent.namesToRemove.contains(mBinding.textChatroomRemoveEmail.getText().toString())) {
                        mParent.namesToRemove.remove(mBinding.textChatroomRemoveEmail.getText().toString());
                    }
                    else{
                        mParent.namesToRemove.add(mBinding.textChatroomRemoveEmail.getText().toString());
                    }
                }
            });
        }




        /**
         * Set contact
         *
         * @param contact Contact the contact object
         */
        void setContact(final Contact contact) {
            mBinding.textChatroomRemoveUsername.setText(contact.getUserName());
            mBinding.textChatroomRemoveEmail.setText(contact.getEmail());
        }

    }
}

