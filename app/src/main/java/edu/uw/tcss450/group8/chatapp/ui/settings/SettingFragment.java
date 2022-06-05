package edu.uw.tcss450.group8.chatapp.ui.settings;

import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.getThemeColor;
import static edu.uw.tcss450.group8.chatapp.utils.ThemeManager.setThemeColor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group8.chatapp.MainActivity;
import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentSettingBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.location.LocationListViewModel;
import edu.uw.tcss450.group8.chatapp.utils.AlertBoxMaker;

/**
 * Class for setting Fragment to handle user settings
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Shilnara Dam
 * @version 5/19/22
 */
public class SettingFragment extends Fragment {
    private UserInfoViewModel mUser;
    private SettingViewModel mSetting;
    private FragmentSettingBinding mBinding;

    /**
     * Required empty public constructor.
     */
    public SettingFragment() {
        // Required empty public constructor\
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        mSetting = new ViewModelProvider(getActivity()).get(SettingViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentSettingBinding.inflate(inflater);
        // Inflate the layout for this fragment
        return mBinding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSetting.getUserInfo(mUser.getJwt());
        mBinding = FragmentSettingBinding.bind(getView());
        mBinding.buttonSettingsChange.setOnClickListener(button2 -> {
            Navigation.findNavController(getView()).navigate(
                    SettingFragmentDirections
                            .actionSettingFragmentToChangeFragment());
        });


        mBinding.buttonSettingsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = AlertBoxMaker.DialogWithStyle(getContext());
                dialog.setTitle("To delete your account permanently click \"Confirm\".")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                mSetting.deleteUserAccount(mUser.getJwt());
                                Navigation.findNavController(getView()).navigate(
                                                SettingFragmentDirections
                                                        .actionSettingFragmentToAuthenticationActivity());
                                final ImageView ToastImageAdd = new ImageView(getActivity());
                                ToastImageAdd.setImageResource(R.drawable.slapchaticon);
                                Toast.makeText(getActivity(), "Your account has been deleted", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show().setCanceledOnTouchOutside(true);
            }
        });


        mSetting.addUserFirstNameObserver(getViewLifecycleOwner(), firstName -> mBinding.textSettingsFirstname.setText("First Name: " + firstName));
        mSetting.addUserLastNameObserver(getViewLifecycleOwner(), lastname -> mBinding.textSettingsLastname.setText("Last Name: " + lastname));
        mSetting.addUserNameObserver(getViewLifecycleOwner(), username -> mBinding.textSettingsUsername.setText("Username: " + username));
        mBinding.textSettingsEmail.setText("Email: " + mUser.getEmail());

        TextView orange = view.findViewById(R.id.text_settings_orangeColor);
        TextView red = view.findViewById(R.id.text_settings_redColor);
        TextView blue = view.findViewById(R.id.text_settings_blueColor);
        TextView green = view.findViewById(R.id.text_settings_greenColor);
        TextView uw = view.findViewById(R.id.text_settings_uwColor);

        mBinding.textSettingsOrangeColor.setOnClickListener(button -> SetColor("orange"));
        mBinding.textSettingsRedColor.setOnClickListener(button -> SetColor("red"));
        mBinding.textSettingsBlueColor.setOnClickListener(button -> SetColor("blue"));
        mBinding.textSettingsGreenColor.setOnClickListener(button -> SetColor("green"));
        mBinding.textSettingsUwColor.setOnClickListener(button -> SetColor("uw"));

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
}