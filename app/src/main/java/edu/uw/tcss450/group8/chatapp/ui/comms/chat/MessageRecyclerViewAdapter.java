
package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentMessageCardBinding;

/**
 * RecyclerViewAdapter for message.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author JenHo Liao
 * @version 1.0
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> {

    //Store all of the blogs to present
    private final List<Message> mMessage;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of message
     */
    public MessageRecyclerViewAdapter(List<Message> items) {
        this.mMessage = items;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mMessage.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mMessage.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentMessageCardBinding binding;
        private Message mMessage;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentMessageCardBinding.bind(view);
        }

        /**
         * Set message
         *
         * @param message message
         */
        void setMessage(final Message message) {
            mMessage = message;
            binding.textMessageMessage.setText(message.getMessage());
        }
    }
}
