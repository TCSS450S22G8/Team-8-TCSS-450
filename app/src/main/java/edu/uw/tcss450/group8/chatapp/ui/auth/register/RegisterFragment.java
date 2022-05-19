package edu.uw.tcss450.group8.chatapp.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group8.chatapp.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group8.chatapp.utils.PasswordValidator;

import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.*;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkClientPredicate;

/**
 * Class for the Register Fragment that handles user registration to the application.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @author Shilnara Dam
 * @author Sean Logan
 * @version 5/12/22
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding mBinding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(mBinding.editRegisterPassword2.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));


    /**
     * Required empty constructor for the register fragment
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegisterBinding.inflate(inflater);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mBinding.buttonRegisterRegister.setOnClickListener(button -> {
            attemptRegister(button);
        });
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse
        );
    }

    /**
     * Attempts to register the new user, validates the user entered information,
     * then sends it to the server for registration validation.
     *
     * @param button button clicked
     */
    private void attemptRegister(final View button) {
        mBinding.layoutWait.setVisibility(View.VISIBLE);
        validateNickname();
    }

    /**
     * Checks if the user input matches the required parameters for nickname.
     * Then calls validate first.
     */
    private void validateNickname() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editRegisterNickname.getText().toString().trim()),
                this::validateFirst,
                result -> {
                    mBinding.editRegisterNickname.setError("Please enter a Nickname.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for first name.
     * Then calls validate last.
     */
    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editRegisterFirst.getText().toString().trim()),
                this::validateLast,
                result -> {
                    mBinding.editRegisterFirst.setError("Please enter a first name.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for last name.
     * Then calls validate email.
     */
    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(mBinding.editRegisterLast.getText().toString().trim()),
                this::validateEmail,
                result -> {
                    mBinding.editRegisterLast.setError("Please enter a last name.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for email.
     * Then calls validate password match.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mBinding.editRegisterEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> {
                    mBinding.editRegisterEmail.setError("Please enter a valid Email address.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for matching passwords.
     * Then calls validate password.
     */
    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(mBinding.editRegisterPassword2.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(mBinding.editRegisterPassword1.getText().toString().trim()),
                this::validatePassword,
                result -> {
                    mBinding.editRegisterPassword1.setError("Passwords must match.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks if the user input matches the required parameters for passwords.
     * Then calls verify auth with server.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editRegisterPassword1.getText().toString()),
                this::verifyAuthWithServer,
                result -> {
                    mBinding.editRegisterPassword1.setError("Please enter a valid Password.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Sends Asynchronous JSON request to the server for user registration
     * information validation.
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                mBinding.editRegisterNickname.getText().toString(),
                mBinding.editRegisterFirst.getText().toString(),
                mBinding.editRegisterLast.getText().toString(),
                mBinding.editRegisterEmail.getText().toString(),
                mBinding.editRegisterPassword1.getText().toString());

        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }


    /**
     * Navigates to the verify fragment to continue registration by verifying email.
     */
    private void navigateToLogin() {
        // ToDO: Register to Verification to autofill login
//        RegisterFragmentDirections.ActionRegisterFragmentToLoginFragment directions =
//                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment();
//
//        directions.setEmail(binding.editEmail.getText().toString());
//        directions.setPassword(binding.editPassword1.getText().toString());

        Navigation.findNavController(getView()).
                navigate(RegisterFragmentDirections.
                        actionRegisterFragmentToVerifyFragment());
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
                    mBinding.editRegisterEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                    mBinding.layoutWait.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    mBinding.layoutWait.setVisibility(View.GONE);
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
            mBinding.layoutWait.setVisibility(View.GONE);
        }
    }
}
