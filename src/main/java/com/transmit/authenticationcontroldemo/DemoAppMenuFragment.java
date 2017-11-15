package com.transmit.authenticationcontroldemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 * Use the {@link DemoAppMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemoAppMenuFragment extends Fragment {
    private Listener mListener;

    public DemoAppMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DemoAppMenuFragment.
     */
    public static DemoAppMenuFragment newInstance() {
        return new DemoAppMenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View res = inflater.inflate(R.layout.fragment_demo_app_menu, container, false);

        final View res2 = res;

//        res.findViewById(R.id.buttonApprovalsMenuItem).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.approvalsSelected();
//                }
//            }
//        });

        res.findViewById(R.id.buttonConfigurationMenuItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.configurationSelected();
                }
            }
        });

//        res.findViewById(R.id.buttonTransferMenuItem).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.transferSelected();
//                }
//            }
//        });

//        res.findViewById(R.id.buttonViewBalanceMenuItem).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.viewBalanceSelected();
//                }
//            }
//        });
//        res.findViewById(R.id.buttonDevicesMenuItem).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mListener != null) {
//                    mListener.devicesSelected();
//                }
//            }
//        });



        res.findViewById(R.id.imageView_quickviewhack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("OnClick quickviewhack");
                view.setVisibility(View.GONE);
                res2.findViewById(R.id.imageView_quickviewhackside).setVisibility(View.VISIBLE);
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
            }
        });

        res.findViewById(R.id.imageView_quickviewhackside).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.imageView_quickviewhackside).setVisibility(View.GONE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.buttonOpenZelle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.buttonOpenZelle).setVisibility(View.GONE);
                res2.findViewById(R.id.zellelanding).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.zellelanding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.zellelanding).setVisibility(View.GONE);
                res2.findViewById(R.id.zellechoose).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.zellechoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.zellechoose).setVisibility(View.GONE);
                res2.findViewById(R.id.zelleinput).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.zelleinput).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.zelleinput).setVisibility(View.GONE);
                res2.findViewById(R.id.zellereviewsend).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.zellereviewsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((ImageView)findViewById(R.id.imageView_quickviewhack)).setVisibility(View.GONE);
                res2.findViewById(R.id.zellereviewsend).setVisibility(View.GONE);

                    if (mListener != null) {
                        //mListener.loginSelected();
                    }

                res2.findViewById(R.id.zelledone).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonConfigurationMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonDevicesMenuItem).setVisibility(View.VISIBLE);
                //res2.findViewById(R.id.buttonLogoutMenuItem).setVisibility(View.VISIBLE);
            }
        });

        res.findViewById(R.id.buttonLogoutMenuItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.logoutSelected();
                }
            }
        });


        return res;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
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
        void devicesSelected();
        void approvalsSelected();
        void configurationSelected();
        void viewBalanceSelected();
        void transferSelected();
        void logoutSelected();
    }
}
