package edu.uw.tcss450.group8.chatapp.ui.auth.forgot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group8.chatapp.databinding.FragmentForgotConfirmlBinding;

/**
 * Class for the Register Fragment that handles user registration to the application.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/19/22
 */
public class ForgotConfirmFragment extends Fragment {

    private FragmentForgotConfirmlBinding mBinding;

    private ForgotViewModel mModel;


    /**
     * Required empty constructor for the register fragment
     */
    public ForgotConfirmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity())
                .get(ForgotViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentForgotConfirmlBinding.inflate(inflater);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = ForgotConfirmFragmentArgs.fromBundle(getArguments()).getEmail();
        mBinding.buttonForgotConfirmResend.setOnClickListener(button -> {
            Toast.makeText(getActivity(), "We'll resend that email!", Toast.LENGTH_SHORT).show();
            mModel.sendForgotPasswordEmail(email);
        });
        mBinding.buttonForgotConfirmLink.setOnClickListener(button -> {
            mModel.sendVerifiedPasswordReset(email);

        });

        mModel.addEmailSuccessObserver(getViewLifecycleOwner(), success -> {
            if(success) {
                Navigation.findNavController(getView()).navigate(
                        ForgotConfirmFragmentDirections.actionForgotConfirmFragmentToForgotFragment(email));

            } else {
                // popup need to confirm email address to continue
                Toast.makeText(getActivity(), "Please Verify your email to continue!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
