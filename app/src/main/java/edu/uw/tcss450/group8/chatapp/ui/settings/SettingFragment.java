package edu.uw.tcss450.group8.chatapp.ui.settings;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setThemeColor;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;

import android.graphics.drawable.LayerDrawable;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;


import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentSettingBinding;

/**
 * Class for setting Fragment to handle user settings
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @version 5/19/22
 */
public class SettingFragment extends Fragment {

    /**
     * Required empty public constructor.
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


        TextView orange = view.findViewById(R.id.text_settings_orangeColor);
        TextView red = view.findViewById(R.id.text_settings_redColor);
        TextView blue = view.findViewById(R.id.text_settings_blueColor);
        TextView green = view.findViewById(R.id.text_settings_greenColor);
        TextView uw = view.findViewById(R.id.text_settings_uwColor);

        binding.textSettingsOrangeColor.setOnClickListener(button -> SetColor("orange"));
        binding.textSettingsRedColor.setOnClickListener(button -> SetColor("red"));
        binding.textSettingsBlueColor.setOnClickListener(button -> SetColor("blue"));
        binding.textSettingsGreenColor.setOnClickListener(button -> SetColor("green"));
        binding.textSettingsUwColor.setOnClickListener(button -> SetColor("uw"));

        if (isDarkMode()) {
            orange.setBackgroundColor(getResources().getColor(R.color.orange_dark, null));
            red.setBackgroundColor(getResources().getColor(R.color.red_dark, null));
            blue.setBackgroundColor(getResources().getColor(R.color.blue_dark, null));
            green.setBackgroundColor(getResources().getColor(R.color.green_dark, null));
        }

        switch (getThemeColor(getActivity())) {
            case "orange":
                SetStroke(orange);
                break;
            case "red":
                SetStroke(red);
                break;
            case "blue":
                SetStroke(blue);
                break;
            case "green":
                SetStroke(green);
                break;
            case "uw":
                if (isDarkMode()) {
                    uw.setBackgroundColor(Color.WHITE);
                } else {
                    uw.setBackgroundColor(Color.BLACK);
                }
                break;

        }

    }


    /**
     * helper method to set color.
     *
     * @param color the color of the theme
     */
    private void SetColor(String color) {
        setThemeColor(getActivity(), color);
        getActivity().recreate();
    }

    /**
     * set the stroke for TextView
     *
     * @param tv
     */
    private void SetStroke(TextView tv) {
        GradientDrawable dg = new GradientDrawable();
        dg.setColor(((ColorDrawable) tv.getBackground()).getColor());
        if (isDarkMode()) {
            dg.setStroke(10, Color.WHITE);
        } else {
            dg.setStroke(10, Color.BLACK);
        }
        tv.setBackground(dg);
    }

    /**
     * check if the device is in dark mode.
     *
     * @return true if dark mode
     */
    private boolean isDarkMode() {
        return (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}