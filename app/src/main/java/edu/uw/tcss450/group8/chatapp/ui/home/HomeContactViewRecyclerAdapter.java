package edu.uw.tcss450.group8.chatapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import edu.uw.tcss450.group8.chatapp.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group8.chatapp.ui.comms.connection.Contact;

/**
 * Recycler View to show all contacts as a list.
 * <p>
 * Adapted from original code by Charles Bryan
 *
 * @author Charles Bryan
 * @author Rin Pham
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/29/22
 */
public class HomeContactViewRecyclerAdapter extends RecyclerView.Adapter<HomeContactViewRecyclerAdapter.ContactViewHolder> {
    private final List<Contact> mContact;
    private final HomeFragment mParent;

    /**
     * Constructor for HomeContactViewRecyclerAdapter
     *
     * @param items  list of contacts
     * @param parent HomeFragment
     */
    public HomeContactViewRecyclerAdapter(List<Contact> items, HomeFragment parent) {
        this.mContact = items;
        mParent = parent;
    }

    @NonNull
    @Override
    public HomeContactViewRecyclerAdapter.ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeContactViewRecyclerAdapter.ContactViewHolder holder, int position) {
        holder.setContact(mContact.get(position));

    }

    @Override
    public int getItemCount() {
        return this.mContact.size();
    }


    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Contacts Recycler View.
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
            mUnFriend = view.findViewById(R.id.button_contact_unfriend);
            messageFriend = view.findViewById(R.id.button_contact_send_message);
            email = view.findViewById(R.id.text_contact_email);
            username = view.findViewById(R.id.text_contact_username);
            mUnFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mParent.getContext());
                    dialog.setTitle("Are you sure you want to remove this contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mParent.homeUnFriend(email.getText().toString());
                                    mContact.remove((getAdapterPosition()));
                                    notifyItemRemoved(getAdapterPosition());
                                    notifyItemRangeChanged(getAdapterPosition(), mContact.size());
                                    Toast.makeText(mParent.getActivity(), "Unfriend success!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show().setCanceledOnTouchOutside(true);
                }
            });

            mBinding.layoutInner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mParent.homeSendMessage(email.getText().toString(), username.getText().toString());
                }
            });
            messageFriend.setVisibility(View.INVISIBLE);
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
