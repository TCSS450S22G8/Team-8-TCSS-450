package edu.uw.tcss450.group8.chatapp.ui.auth.login;

import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.getEmail;
import static edu.uw.tcss450.group8.chatapp.utils.LogInStatusManager.getJWT;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkExcludeWhiteSpace;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdLength;
import static edu.uw.tcss450.group8.chatapp.utils.PasswordValidator.checkPwdSpecialChar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLoginBinding;
import edu.uw.tcss450.group8.chatapp.model.PushyTokenViewModel;
import edu.uw.tcss450.group8.chatapp.model.UserInfoViewModel;
import edu.uw.tcss450.group8.chatapp.ui.auth.verify.VerifyFragmentArgs;
import edu.uw.tcss450.group8.chatapp.ui.weather.WeatherFragmentArgs;
import edu.uw.tcss450.group8.chatapp.utils.AlertBoxMaker;
import edu.uw.tcss450.group8.chatapp.utils.PasswordValidator;

/**
 * Class for Login Fragment handles user login to application.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Sean Logan
 * @author Shilnara Dam
 * @author Levi McCoy
 * @version 6/1/22
 */
public class LoginFragment extends Fragment {

    private FragmentLoginBinding mBinding;
    private LoginViewModel mSignInModel;

    private PushyTokenViewModel mPushyTokenViewModel;
    private UserInfoViewModel mUserViewModel;

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
        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
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

        //String jwt = getJWT(getActivity());
        //String email = getEmail(getActivity());
//        if(!jwt.equals("")||!email.equals("")){
//            Navigation.findNavController(getView())
//                    .navigate(LoginFragmentDirections
//                            .actionLoginFragmentToMainActivity(email, jwt));
//            getActivity().finish();
//        }
        LoginFragmentArgs args = LoginFragmentArgs.fromBundle(getArguments());

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        mBinding = FragmentLoginBinding.bind(getView());

        mBinding.buttonLoginRegister.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        mBinding.buttonLoginForgot.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToForgotEmailFragment()
                ));

        //On button click, navigate to MainActivity
        mBinding.buttonLoginLogin.setOnClickListener(this::attemptSignIn);

        mSignInModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observeResponse);

        //autofills user info when registering or forgetting password
        mBinding.editRegisterEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        mBinding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                mBinding.buttonLoginLogin.setEnabled(!token.isEmpty()));

        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    /**
     * Attempts sign in, validates user email and password
     * then sends to the server for actual login validation.
     *
     * @param button button clicked
     */
    private void attemptSignIn(final View button) {
        mBinding.progressBar.setVisibility(View.VISIBLE);
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
                    // These don't actually do anything we get the response from the server
                    AlertDialog.Builder dialog = AlertBoxMaker.DialogWithStyle(getContext());
                    dialog.setTitle("Please enter a valid Email address.")
                            .setNegativeButton("Okay", null)
                            .show().setCanceledOnTouchOutside(true);
                    mBinding.progressBar.setVisibility(View.GONE);
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
                    mBinding.progressBar.setVisibility(View.GONE);
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
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt().toString());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                AlertDialog.Builder dialog = AlertBoxMaker.DialogWithStyle(getContext());
                dialog.setTitle("Error Authenticating on Push Token. Please contact support")
                        .setNegativeButton("Okay", null)
                        .show().setCanceledOnTouchOutside(true);

            } else {
                navigateToSuccess(
                        mBinding.editRegisterEmail.getText().toString(),
                        mUserViewModel.getJwt().toString()
                );
            }
        }
    }

    /**
     * Helper to abstract the navigation to the Activity past Authentication.
     *
     * @param email users email
     * @param jwt   the JSON Web Token supplied by the server
     */
    private void navigateToSuccess(final String email, final String jwt) {
        if (mBinding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }

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
                    AlertDialog.Builder dialog = AlertBoxMaker.DialogWithStyle(getContext());
                    dialog.setTitle("Error Authenticating: " + response.getJSONObject("data").getString("message"))
                            .setNegativeButton("Okay", null)
                            .show().setCanceledOnTouchOutside(true);
                    mBinding.progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    mBinding.editRegisterEmail.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoViewModel.class);

                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                    mBinding.progressBar.setVisibility(View.GONE);
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
            mBinding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                navigateToSuccess(email, token);
                return;
            }
        }
    }
}