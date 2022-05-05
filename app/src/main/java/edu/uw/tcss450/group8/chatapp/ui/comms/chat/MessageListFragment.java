/**
 * @author jliaoh
 * @version 1.0
 */
package edu.uw.tcss450.group8.chatapp.ui.comms.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentMessageListBinding;

/**
 * create an instance of Message List fragment.
 */
public class MessageListFragment extends Fragment {
    private MessageListViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(MessageListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());
        mModel.addBlogListObserver(getViewLifecycleOwner(), blogList -> {
            if (!blogList.isEmpty()) {

                binding.listRoot.setAdapter(
                        new MessageRecyclerViewAdapter(MessageGenerator.getMessageList())
                );
//                binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }
}