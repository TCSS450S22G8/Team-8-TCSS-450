package edu.uw.tcss450.group8.chatapp.ui.settings.change;

import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkClientPredicate;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdDigit;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdLowerCase;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdSpecialChar;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdUpperCase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group8.chatapp.databinding.FragmentChangeBinding;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.utils.PasswordValidator;

/**
 * Class for the Register Fragment that handles user registration to the application.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 5/19/22
 */
public class ChangeFragment extends Fragment {

    private FragmentChangeBinding mBinding;

    private ChangeViewModel mChangeModel;



    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(mBinding.editChangePassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    private PasswordValidator mPassWordValidator2 =
            checkClientPredicate(pwd -> pwd.equals(mBinding.editChangeCurPass.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));



    /**
     * Required empty constructor for the register fragment
     */
    public ChangeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangeModel = new ViewModelProvider(getActivity())
                .get(ChangeViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChangeBinding.inflate(inflater);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBinding.buttonChange.setOnClickListener(button -> {
            attemptChange(button);
            //Navigation.findNavController(getView()).navigate(
            //RegisterFragmentDirections.actionRegisterFragmentToVerifyFragment());
        });
        mChangeModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse
        );



    }

    /**
     * Attempts to Chane to the new password, validates the user entered information,
     * then sends it to the server for validation.
     *
     * @param button button clicked
     */
    private void attemptChange(final View button) {
        mBinding.layoutWait.setVisibility(View.VISIBLE);
        validatePasswordsMatch();
    }


    /**
     * Checks if the user input matches the required parameters for matching passwords.
     * Then calls validate password.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(mBinding.editChangePassword2.getText().toString().trim()));

        mPassWordValidator.processResult(
                matchValidator.apply(mBinding.editChangePassword1.getText().toString().trim()),
                this::validatePassword,
                result -> {
                    mBinding.editChangePassword2.setError("Passwords must match.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for passwords.
     * Then calls cahck for old password.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editChangePassword1.getText().toString()),
                this::validatePassword2,
                result -> {
                    mBinding.editChangePassword1.setError("Please enter a valid Password 1.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for passwords.
     * Then calls verify auth with server.
     */
    private void validatePassword2() {
        mPassWordValidator2.processResult(
                mPassWordValidator2.apply(mBinding.editChangeCurPass.getText().toString()),
                this::verifyAuthWithServer,
                result -> {
                    mBinding.editChangeCurPass.setError("Please enter a valid Password 2.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Sends Asynchronous JSON request to the server for user registration
     * information validation.
     */
    private void verifyAuthWithServer() {
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        mChangeModel.connect(
                model.getJwt().toString(),
                mBinding.editChangeCurPass.getText().toString(),
                mBinding.editChangePassword1.getText().toString());


        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().


    }


    /**
     * Navigates to the verify fragment to continue registration by verifying email.
     */
    private void navigateToSettings() {
        // ToDO: Register to Verification to autofill login
//        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
//                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
//
//        directions.setEmail(binding.editEmail.getText().toString());
//        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).navigate(ChangeFragmentDirections.actionChangeFragmentToNavHomeFragment());

    }


    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    mBinding.editChangeCurPass.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                    mBinding.layoutWait.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    mBinding.layoutWait.setVisibility(View.GONE);
                }
            } else {
                navigateToSettings();
            }
        } else {
            Log.d("JSON Response", "No Response");
            mBinding.layoutWait.setVisibility(View.GONE);
        }

    }



}
