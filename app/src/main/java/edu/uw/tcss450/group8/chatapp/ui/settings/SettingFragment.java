package edu.uw.tcss450.group8.chatapp.ui.settings;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setThemeColor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentSettingBinding;

/**
 * Class for setting Fragment to handle user settings
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @version 1.0
 */
public class SettingFragment extends Fragment {

    /**
     * Required empty public contstructor.
     */
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
        binding.buttonSettingsChange.setOnClickListener(button2 -> {
            Navigation.findNavController(getView()).navigate(
                    SettingFragmentDirections
                            .actionSettingFragmentToChangeFragment());
        });
        //on button click,navigate to settings
        // TODO once login system is done, remove signed in user info.
        binding.buttonSettingsLogout.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(
                    SettingFragmentDirections
                            .actionSettingFragmentToAuthenticationActivity());
            //This tells the containing Activity that we are done with it.
            //It will not be added to backstack.
            getActivity().finish();
        });
        binding.textSettingsOrangeColor.setOnClickListener(button -> SetColor("orange"));
        binding.textSettingsRedColor.setOnClickListener(button -> SetColor("red"));
        binding.textSettingsBlueColor.setOnClickListener(button -> SetColor("blue"));
        binding.textSettingsGreenColor.setOnClickListener(button -> SetColor("green"));

    }


    private void SetColor(String color) {
        setThemeColor(getActivity(), color);
        getActivity().recreate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}