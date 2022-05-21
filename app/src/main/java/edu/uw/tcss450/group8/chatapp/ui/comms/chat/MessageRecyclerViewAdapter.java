
package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import android.content.res.Resources;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentMessageCardBinding;

/**
 * RecyclerViewAdapter for message.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author JenHo Liao
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/19/22
 */
public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder> {

    private final List<Message> mMessages;
    private final String mEmail;
    private int mColor;

    /**
     * Constructor for View Adaptor.
     *
     * @param messages List
     * @param email    String
     */
    public MessageRecyclerViewAdapter(List<Message> messages, String email, int color) {
        this.mMessages = messages;
        mEmail = email;
        mColor = color;
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
        holder.setMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mMessages.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private FragmentMessageCardBinding binding;

        /**
         * Constructor for View Holder.
         *
         * @param view View
         */
        public MessageViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            binding = FragmentMessageCardBinding.bind(view);
        }

        /**
         * Sets each card message.
         *
         * @param message Message
         */
        void setMessage(final Message message) {
            final Resources res = mView.getContext().getResources();
            final CardView card = binding.cardRoot;

            int standard = (int) res.getDimension(R.dimen.chat_margin);
            int extended = (int) res.getDimension(R.dimen.chat_margin_sided);

            if (mEmail.equals(message.getSender())) {
                //This message is from the user. Format it as such
                binding.textMessage.setText(message.getMessage());
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();
                //Set the left margin
                layoutParams.setMargins(extended, standard, standard, standard);
                // Set this View to the right (end) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                        Gravity.END;


                card.setCardBackgroundColor(res.getColor(R.color.gray, null));

                card.requestLayout();
            } else {
                //This message is from another user. Format it as such
                binding.textMessage.setText(message.getSender() +
                        ": " + message.getMessage());
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) card.getLayoutParams();

                //Set the right margin
                layoutParams.setMargins(standard, standard, extended, standard);
                // Set this View to the left (start) side
                ((FrameLayout.LayoutParams) card.getLayoutParams()).gravity =
                        Gravity.START;

                card.setCardBackgroundColor(mColor);
                //Round the corners on the right side
                card.requestLayout();
            }
        }
    }
}
