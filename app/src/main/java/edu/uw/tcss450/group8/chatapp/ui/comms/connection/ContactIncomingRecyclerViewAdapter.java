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

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactIncomingRequestCardBinding;

/**
 * recycler for incoming requests
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @version 5/31/22
 */
public class ContactIncomingRecyclerViewAdapter extends RecyclerView.Adapter<ContactIncomingRecyclerViewAdapter.IncomingContactViewHolder> {
    private List<Contact> mContactRequest;
    private final ContactRequestFragment mParentRequest;


    /**
     * Constructor for ContactIncomingRecyclerViewAdapter
     *
     * @param items List<Contact> list of contacts
     * @param parent ContactRequestFragment the parent class
     */
    public ContactIncomingRecyclerViewAdapter(List<Contact> items, ContactRequestFragment parent) {
        this.mContactRequest = items;
        this.mParentRequest = parent;
    }

    @NonNull
    @Override
    public ContactIncomingRecyclerViewAdapter.IncomingContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactIncomingRecyclerViewAdapter.IncomingContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_incoming_request_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactIncomingRecyclerViewAdapter.IncomingContactViewHolder holder, int position) {
        holder.setContact(mContactRequest.get(position));

    }

    @Override
    public int getItemCount() {
        return mContactRequest.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class IncomingContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactIncomingRequestCardBinding mBinding;
        public Button mAcceptFriend;
        public Button mDeclineFriend;
        public TextView email;
        public TextView username;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public IncomingContactViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentContactIncomingRequestCardBinding.bind(view);
            mAcceptFriend = mBinding.buttonContactAcceptFriend;
            mDeclineFriend = mBinding.buttonDeleteRequest;
            email = mBinding.textContactEmail;
            username = mBinding.textContactUsername;

            //button to accept friend request
            mAcceptFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParentRequest.acceptFriendRequest(email.getText().toString());
                    mContactRequest.remove((getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mContactRequest.size());
                    Toast.makeText(mParentRequest.getActivity(), "Accepted friend request successful!", Toast.LENGTH_SHORT).show();
                }
            });

            //button to decline friend request
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
            mBinding.textContactUsername.setText(contact.getUserName());
            mBinding.textContactEmail.setText(contact.getEmail());
        }
    }
}
