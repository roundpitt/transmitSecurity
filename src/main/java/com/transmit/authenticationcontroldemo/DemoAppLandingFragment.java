package com.transmit.authenticationcontroldemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ts.sdk.api.SDK;

/**
 * Created by eran on 13/10/2016.
 */

public class DemoAppLandingFragment extends Fragment {
    private Listener mListener;

    private View mRootView;
    private Button mChangeMethodButton, mChangeUserButton;

    public DemoAppLandingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DemoAppLandingFragment.
     */
    public static DemoAppLandingFragment newInstance() {
        return new DemoAppLandingFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_demo_app_landing, container, false);

        mRootView.findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.loginSelected();
                }
            }
        });

        mChangeUserButton = (Button) mRootView.findViewById(R.id.change_user_btn);

        mChangeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.changeUserSelected();
                }
            }
        });

        mChangeMethodButton = (Button) mRootView.findViewById(R.id.change_auth_method_btn);

        mChangeMethodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.changeAuthMethodSelected();
                }
            }
        });

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        String username = SDK.getInstance().getCurrentUsername();
        String message;

        if (null == username) {
            message = getString(R.string._APP_landing_fragment_no_user_message);
            mChangeMethodButton.setVisibility(View.INVISIBLE);
            mChangeUserButton.setVisibility(View.INVISIBLE);
        }
        else {
            message = getString(R.string._APP_landing_fragment_stored_user_message) + " " + username;
            mChangeMethodButton.setVisibility(View.VISIBLE);
            mChangeUserButton.setVisibility(View.VISIBLE);
        }

        //TextView messageTextView = (TextView) mRootView.findViewById(R.id.welcome_text_view);
        //messageTextView.setText(message);

        mRootView.findViewById(R.id.dev_options_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.devOptionsSelected();
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DemoAppLandingFragment.Listener) {
            mListener = (DemoAppLandingFragment.Listener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        void loginSelected();
        void changeUserSelected();
        void changeAuthMethodSelected();
        void devOptionsSelected ();
    }

}
