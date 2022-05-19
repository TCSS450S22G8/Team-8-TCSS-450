package edu.uw.tcss450.group8.chatapp.ui.auth.forgot;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * Class for making the view model for the register Fragment.
 * Adapted from original code by Charles Bryan.
 *
 * @author Charles Bryan
 * @author Levi McCoy
 * @version 1.0
 */
public class ForgotViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    private MutableLiveData<Boolean> mSuccessfulResponse;

    /**
     * Constructor that instantiates the register View Model
     *
     * @param application top level application
     */
    public ForgotViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mSuccessfulResponse = new MutableLiveData<>();
        mSuccessfulResponse.setValue(false);
    }

    /**
     * Adds response observer to the register fragment.
     *
     * @param owner    current lifecycle
     * @param observer observes live data
     */
    public void addEmailSuccessObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Boolean> observer) {
        mSuccessfulResponse.observe(owner, observer);

    }

    /**
     *
     *
     * @param error
     */
    private void handleEmailVerificationError(final VolleyError error) {
        mSuccessfulResponse.setValue(false);

    }


    /**
    * Handles JSON Request Errors and volley errors
    *
    * @param error error that is given to handle
    *
    */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        } else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                JSONObject response = new JSONObject();
                response.put("code", error.networkResponse.statusCode);
                response.put("data", new JSONObject(data));
                mResponse.setValue(response);
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * do it
     *
     * @param success
     */
    private void successfulResetPasswordVerification(JSONObject success) {
        mSuccessfulResponse.setValue(true);
    }

    /**
     * Sends JSON Request to the server for the forgot password verification process.
     *
     * @param userEmail    users email
     */
    public void sendForgotPasswordEmail(final String userEmail) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/forgot-password/" + userEmail;

        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Sends JSON Request to the server to check if the user has clicked the link in the
     * verification email we have sent. If they have clicked the link, the database updates
     * the forgotPassVerification flag from a 0 to a 1.
     *
     * @param userEmail    users email
     */
    public void sendVerifiedPasswordReset(final String userEmail) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/forgot-password/check-flag/" + userEmail;

        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::successfulResetPasswordVerification,
                this::handleEmailVerificationError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Sends JSON Request to the server reset the users password.
     *
     * @param userNewPassword    users new password
     */
    public void resetUserPassword(final String email, final String userNewPassword) {
        String url = "https://tcss-450-sp22-group-8.herokuapp.com/forgot-password/new-password/";
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("password", userNewPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Request request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                body,
                mResponse::setValue,
                this::handleError);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }

    /**
     * Getter for response
     * @return
     */
    public boolean getSuccessResponse() {
        return mSuccessfulResponse.getValue().booleanValue();
    }


}
