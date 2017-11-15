package com.transmit.authenticationcontroldemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 * Use the {@link TransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferFragment extends Fragment {

    private Listener mListener;

    public TransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TransferFragment.
     */
    public static TransferFragment newInstance() {
        return new TransferFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_demo_app_transfer, container, false);
        final EditText toEd = (EditText)view.findViewById(R.id._TS_authcontrol_demo_transfer_to_ed);
        final EditText amountEd = (EditText)view.findViewById(R.id._TS_authcontrol_demo_transfer_amount_ed);
        final EditText commentEd = (EditText)view.findViewById(R.id._TS_authcontrol_demo_transfer_comment_ed);

        view.findViewById(R.id._TS_authcontrol_demo_transfer_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = validateNotEmpty(toEd);
                String amount = validateNotEmpty(amountEd);
                String comment = validateNotEmpty(commentEd);
                if (mListener != null && to != null && amount != null && comment != null) {
                    InputMethodManager imm = (InputMethodManager)TransferFragment.this.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    mListener.doTransferSelected(to, Integer.parseInt(amount), comment);
                }
            }
        });

        view.findViewById(R.id._TS_authcontrol_demo_transfer_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.cancelTransferSelected();
                }
            }
        });

        return view;
    }

    private String validateNotEmpty(EditText _ed) {
        String res = _ed.getText().toString();
        if (res.equals("")) {
            _ed.setError("Mandatory field");
            return null;
        }
        return res;
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
        void doTransferSelected(CharSequence _to, int _amount, CharSequence _comment);
        void cancelTransferSelected ();
    }
}
