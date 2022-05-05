package edu.uw.tcss450.group8.chatapp.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLoginBinding;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentSettingBinding;
import edu.uw.tcss450.group8.chatapp.ui.auth.login.LoginFragmentDirections;
import edu.uw.tcss450.group8.chatapp.ui.home.HomeFragmentDirections;

/**
 * Class for setting Fragment to handle user settings
 *
 * @author Shilnara Dam
 * @version 1.0
 */
public class SettingFragment extends Fragment {

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentSettingBinding binding = FragmentSettingBinding.bind(getView());
        //on button click,navigate to settings
        // TODO once login system is done, remove signed in user info.
        binding.buttonSignout.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    SettingFragmentDirections
                            .actionSettingFragmentToAuthenticationActivity());
            //This tells the containing Activity that we are done with it.
            //It will not be added to backstack.
            getActivity().finish();
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}