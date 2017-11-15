/*
 * Copyright (C) Transmit Security, LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.transmit.authenticationcontroldemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DevOptionsFragment extends Fragment {

    public interface Listener {
        void oobConfigurationSelected ();
        void backFromDevOptionsSelected ();
    }

    private Listener mListener;

    public static DevOptionsFragment newInstance() {
        return new DevOptionsFragment();
    }

    public DevOptionsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_demo_app_dev_options, container, false);

        view.findViewById(R.id.oob_config_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.oobConfigurationSelected();
            }
        });

        view.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backFromDevOptionsSelected();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
