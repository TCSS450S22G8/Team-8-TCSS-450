package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactAddContactBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactAddContactCardBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;

public class AddContactRecyclerViewAdapter extends RecyclerView.Adapter<AddContactRecyclerViewAdapter.AddContactViewHolder> {
    private List<Contact> mContact;
    private final AddContactFragment mParent;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    public AddContactRecyclerViewAdapter(List<Contact> items, AddContactFragment parent) {
        this.mContact= items;
        mParent = parent;
    }

    @NonNull
    @Override
    public AddContactRecyclerViewAdapter.AddContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_add_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddContactRecyclerViewAdapter.AddContactViewHolder holder, int position) {
        holder.setContact(mContact.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContact.size();
    }

    public void contactList(ArrayList<Contact> contactList) {
        mContact = contactList;
        notifyDataSetChanged();
    }


    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class AddContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactAddContactCardBinding mBinding;
        public Button mAddFriend;
        public TextView email;
        public TextView username;

        /**
         * Constructor for View Holder
         *
         * @param view View
         */
        public AddContactViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentContactAddContactCardBinding.bind(view);
            mAddFriend = view.findViewById(R.id.button_contact_add_friend);
            email = view.findViewById(R.id.text_contact_email);
            username = view.findViewById(R.id.text_contact_username);

            mAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.addFriend(email.getText().toString());
                    mContact.remove((getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), mContact.size());
                    Toast.makeText(mParent.getActivity(), "Sent request successful!", Toast.LENGTH_SHORT).show();
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