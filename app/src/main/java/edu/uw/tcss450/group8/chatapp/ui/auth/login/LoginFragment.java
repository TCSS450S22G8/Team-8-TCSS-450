package edu.uw.tcss450.group8.chatapp.ui.auth.login;

import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.getEmail;
import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.getJWT;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

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

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLoginBinding;
import edu.uw.tcss450.group8.chatapp.utils.PasswordValidator;

/**
 * Class for Login Fragment handles user login to application.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Sean Logan
 * @author Shilnara Dam
 * @version 1.0
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;
    private LoginViewModel mSignInModel;

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator = checkPwdLength(1)
            .and(checkExcludeWhiteSpace());

    /**
     * Required empty constructor for login fragment
     */
    public LoginFragment() {
        // Required Empty Constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity())
                .get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String jwt = getJWT(getActivity());
        String email = getEmail(getActivity());

        if(!jwt.equals("")||!email.equals("")){
            Navigation.findNavController(getView())
                    .navigate(LoginFragmentDirections
                            .actionLoginFragmentToMainActivity(email, jwt));
            getActivity().finish();
        }

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        mBinding = FragmentLoginBinding.bind(getView());

        mBinding.buttonLoginRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        mBinding.buttonLoginForgot.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToForgotFragment()
                ));

        //On button click, navigate to MainActivity
        mBinding.buttonLoginLogin.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());
        mBinding.editRegisterEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Attempts sign in, validates user email and password
     * then sends to the server for actual login validation.
     *
     * @param button button clicked
     */
    private void attemptSignIn(final View button) {
        mBinding.layoutWait.setVisibility(View.VISIBLE);
        validateEmail();
    }

    /**
     * Checks user input for email to match required parameters.
     * Calls validate password.
     */
    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(mBinding.editRegisterEmail.getText().toString().trim()),
                this::validatePassword,
                result -> {
                    mBinding.editRegisterEmail.setError("Please enter a valid Email address.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });
    }

    /**
     * Checks user input for password to match required parameters.
     * Sends to server for actual login.
     */
    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(mBinding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> {
                    mBinding.editPassword.setError("Please enter a valid Password.");
                    mBinding.layoutWait.setVisibility(View.GONE);
                });

    }

    /**
     * Sends JSON request to server for user login validation.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                mBinding.editRegisterEmail.getText().toString(),
                mBinding.editPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param email users email
     * @param jwt   the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        Navigation.findNavController(getView())
                .navigate(LoginFragmentDirections
                        .actionLoginFragmentToMainActivity(email, jwt));
        getActivity().finish();
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
                try {
                    navigateToSuccess(
                            mBinding.editRegisterEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    mBinding.layoutWait.setVisibility(View.GONE);
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            mBinding.layoutWait.setVisibility(View.GONE);
        }

    }


}