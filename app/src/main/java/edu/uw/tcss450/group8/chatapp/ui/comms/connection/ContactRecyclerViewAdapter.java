package edu.uw.tcss450.group8.chatapp.ui.comms.connection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.chat.Message;


public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder>{
    private final List<Contact> mContact;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    public ContactRecyclerViewAdapter(List<Contact> items) {
        this.mContact= items;
    }

    @NonNull
    @Override
    public ContactRecyclerViewAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactRecyclerViewAdapter.ContactViewHolder(LayoutInflater
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
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentContactCardBinding mBinding;
        private Contact mContact;

        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            mBinding = FragmentContactCardBinding.bind(view);
        }

        /**
         * Set contact
         *
         * @param
         */
        void setContact(final Contact contact) {
            mContact = contact;
            mBinding.textContactUsername.setText(contact.getUserName());
        }
    }
}
