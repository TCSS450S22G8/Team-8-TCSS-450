package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactIncomingRequestCardBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactOutgoingRequestCardBinding;

public class ContactOutgoingRecyclerViewAdapter extends RecyclerView.Adapter<ContactOutgoingRecyclerViewAdapter.OutgoingContactViewHolder>{
    private List<Contact> mContactRequest;
    private final ContactRequestFragment mParentRequest;


    /**
     * Contructor for ContactIncomingRecyclerViewAdapter
     *
     * @param items
     * @param parent
     * @return
     */
    public ContactOutgoingRecyclerViewAdapter(List<Contact> items, ContactRequestFragment parent) {
        this.mContactRequest = items;
        this.mParentRequest = parent;
    }

    @NonNull
    @Override
    public ContactOutgoingRecyclerViewAdapter.OutgoingContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OutgoingContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_outgoing_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactOutgoingRecyclerViewAdapter.OutgoingContactViewHolder holder, int position) {
        holder.setContact(mContactRequest.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContactRequest.size();
    }

    public void contactList(ArrayList<Contact> contactList) {
        mContactRequest = contactList;
        notifyDataSetChanged();
    }

    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class OutgoingContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactOutgoingRequestCardBinding mBinding;
        public Button mDeclineFriend;
        public TextView email;
        public TextView username;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public OutgoingContactViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentContactOutgoingRequestCardBinding.bind(view);
            mDeclineFriend = mBinding.buttonDeleteRequest;
            email = mBinding.textContactEmail;
            username = mBinding.textContactUsername;

            mDeclineFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParentRequest.deleteFriendRequest(email.getText().toString());
                    mContactRequest.remove((getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mContactRequest.size());
                    Toast.makeText(mParentRequest.getActivity(), "Deleted friend request successful!", Toast.LENGTH_SHORT).show();
                }
            });

        }

        /**
         * Set contact
         *
         * @param contact Contact the contact object
         */
        void setContact(final Contact contact) {
            Log.e("outgoing", "seting individual card" );
            mBinding.textContactUsername.setText(contact.getUserName());
            mBinding.textContactEmail.setText(contact.getEmail());
        }
    }
}
