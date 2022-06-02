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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomAddUserCardBinding;
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
 * @version 6/2/22
 */

public class ChatroomAddUserRecyclerViewAdapter extends RecyclerView.Adapter<ChatroomAddUserRecyclerViewAdapter.ChatroomAddUserViewHolder> {
    private final List<Contact> mContact;
    private final ChatroomAddUserFragment mParent;



    /**
     * Constructor for AdduserRecyclerViewAdapter
     *
     * @param items list of message
     */

    public ChatroomAddUserRecyclerViewAdapter(List<Contact> items, ChatroomAddUserFragment parent) {
        this.mContact= items;
        mParent = parent;
    }


    @NonNull
    @Override
    public ChatroomAddUserRecyclerViewAdapter.ChatroomAddUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomAddUserViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_add_user_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomAddUserRecyclerViewAdapter.ChatroomAddUserViewHolder holder, int position) {
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

    public class ChatroomAddUserViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatroomAddUserCardBinding mBinding;

        public CheckBox mAdd;

        public TextView email;
        public EditText name;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */

        public ChatroomAddUserViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentChatroomAddUserCardBinding.bind(view);
            mAdd = view.findViewById(R.id.checkBox_add_user);


            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mParent.namesToAdd.contains(mBinding.textChatroomAddUserEmail.getText().toString())) {
                        mParent.namesToAdd.remove(mBinding.textChatroomAddUserEmail.getText().toString());
                        Log.e("emailList", mParent.namesToAdd.toString());
                    }
                    else{
                        mParent.namesToAdd.add(mBinding.textChatroomAddUserEmail.getText().toString());
                        Log.e("emailList", mParent.namesToAdd.toString());
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
            mBinding.textChatroomAddUserUsername.setText(contact.getUserName());
            mBinding.textChatroomAddUserEmail.setText(contact.getEmail());
        }

    }
}

