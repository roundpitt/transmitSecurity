/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo.baseactivities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.transmit.authenticationcontroldemo.R;

/**
 * Created by eran on 9/21/15.
 */
public class PlaceholderPasswordInputFragment extends Fragment {

    interface Listener {
        void setPasswordFromPasswordFragment(String _password);
        void changeMethodFromPasswordFragment();
        String getUsername ();
    }

    private EditText mPasswordEditText;
    private Listener mListener;

    private void hideKeyboard () {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        View focusedView = getActivity().getCurrentFocus();

        if (null == focusedView) {
            focusedView = new View(getActivity());
        }

        imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_demo_app_password_input, null);

        mListener = (Listener)getActivity();

        ((EditText) view.findViewById(R.id._APP_login_username_ed)).setText(mListener.getUsername());

        mPasswordEditText = (EditText) view.findViewById(R.id._APP_login_password_ed);

        view.findViewById(R.id._APP_authcontroldemo_change_method_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.changeMethodFromPasswordFragment();
            }
        });

        Button loginButton = (Button) view.findViewById(R.id._APP_authcontroldemo_login_btn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mPasswordEditText.getText().toString();

                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), R.string._APP_credentials_password_error_message, Toast.LENGTH_LONG).show();
                    return;
                }
                mListener.setPasswordFromPasswordFragment(password);

                hideKeyboard();
            }
        });

        mPasswordEditText.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard();
    }

}
