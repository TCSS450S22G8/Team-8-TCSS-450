
package edu.uw.tcss450.group8.chatapp.ui.comms.chatrooms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentChatroomCardBinding;

/**
 * RecyclerViewAdapter for message.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 1.0
 */
public class ChatroomRecyclerViewAdapter extends RecyclerView.Adapter<ChatroomRecyclerViewAdapter.ChatroomViewHolder> {

    //Store all of the blogs to present
    private final List<Chatroom> mChatroom;

    /**
     * Constructor for MessageRecyclerViewAdapter
     *
     * @param items list of chatroom
     */
    public ChatroomRecyclerViewAdapter(List<Chatroom> items) {
        this.mChatroom = items;
    }

    @NonNull
    @Override
    public ChatroomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatroomViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_chatroom_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatroomViewHolder holder, int position) {
        holder.setChatroom(mChatroom.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mChatroom.size();
    }

    /**
     * Objects from this class represent an Individual row View from the List * of rows in the
     * Message Recycler View.
     */
    public class ChatroomViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatroomCardBinding binding;
        private Chatroom mChatroom;

        public ChatroomViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatroomCardBinding.bind(view);
        }

        /**
         * Set message
         *
         * @param chatroom chatroom
         */
        void setChatroom(final Chatroom chatroom) {
            mChatroom = chatroom;
           // binding.textMessageMessage.setText(chatroom.getMessage());
        }
    }
}
