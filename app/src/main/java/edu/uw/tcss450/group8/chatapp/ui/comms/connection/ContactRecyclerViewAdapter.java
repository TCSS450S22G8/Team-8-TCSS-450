package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;

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
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {
    private List<Contact> mContact;
    private final ContactFragment mParent;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    public ContactRecyclerViewAdapter(List<Contact> items, ContactFragment parent) {
        this.mContact= items;
        mParent = parent;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactRecyclerViewAdapter.ContactViewHolder holder, int position) {
        holder.setContact(mContact.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContact.size();
    }

    /**
     * sets contact list
     *
     * @param contactList ArrayList<Contact> the contact list
     */
    public void contactList(ArrayList<Contact> contactList) {
        Log.e("error", "contactList: ");
        mContact = contactList;
        notifyDataSetChanged();
    }


    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactCardBinding mBinding;

        public Button mUnFriend;
        public Button messageFriend;


        public TextView email;
        public TextView username;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentContactCardBinding.bind(view);
            mUnFriend = mBinding.buttonContactUnfriend;
            messageFriend = mBinding.buttonContactSendMessage;
            email = mBinding.textContactEmail;
            username = mBinding.textContactUsername;

            //button to unfriend friend
            mUnFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mParent.getContext());
                    dialog.setTitle("Are you sure you want to remove this contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mParent.unFriend(email.getText().toString());
                                    mContact.remove((getAdapterPosition()));
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), mContact.size());
                                    Toast.makeText(mParent.getActivity(), "Unfriend success!", Toast.LENGTH_SHORT).show();                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show().setCanceledOnTouchOutside(true);
                }
            });

            //button to message friend
            messageFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.sendMessage(email.getText().toString(), username.getText().toString());
                }
            });
        }

        /**
         * Set contact
         *
         * @param contact Contact the contact object
         */
        void setContact(final Contact contact) {
            mBinding.textContactUsername.setText(contact.getUserName());
            mBinding.textContactEmail.setText(contact.getEmail());
        }
    }
}
