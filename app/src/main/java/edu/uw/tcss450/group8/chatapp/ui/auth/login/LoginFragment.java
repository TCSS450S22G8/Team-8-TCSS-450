package edu.uw.tcss450.group8.chatapp.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import edu.uw.tcss450.group8.chatapp.R;
import edu.uw.tcss450.group8.chatapp.databinding.FragmentLoginBinding;
import edu.uw.tcss450.group8.chatapp.ui.auth.register.RegisterFragment;


public class LoginFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentLoginBinding binding = FragmentLoginBinding.bind(getView());

        binding.buttonRegisterLogin.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                ));

        //On button click, navigate to MainActivity
        binding.buttonSignin.setOnClickListener(button -> {

            Navigation.findNavController(getView()).navigate(
                    LoginFragmentDirections
                            .actionSignInFragmentToMainActivity("Not passing anything for now, COME BACK"));

            //This tells the containing Activity that we are done with it.
            //It will not be added to backstack.
            getActivity().finish();
        });
    }





}