package org.androidtown.mybikes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NonGuestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NonGuestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NonGuestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Activity activity;
    private TextView bikerental_tv;
    private TextView return_tv;

    public NonGuestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NonGuestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NonGuestFragment newInstance(String param1, String param2) {
        NonGuestFragment fragment = new NonGuestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_non_guest, container, false);
        bikerental_tv = (TextView) rootView.findViewById(R.id.bikerental_tv);
        return_tv = (TextView) rootView.findViewById(R.id.return_tv);

        bikerental_tv.setText("<'자전거 대여방법' 자세히 보기>");
        return_tv.setText("<'자전거 반납' 자세히 보기>");

        bikerental_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(bikerental_tv.getClass()==v.getClass()){
                        bikerental_tv.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    if(bikerental_tv.getClass()==v.getClass()){
                        bikerental_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(bikerental_tv.getClass()==v.getClass()){
                        bikerental_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                        Intent intent = new Intent(activity, BikeRentalTapActivity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });

        return_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    if(return_tv.getClass()==v.getClass()){
                        return_tv.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    if(return_tv.getClass()==v.getClass()){
                        return_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(return_tv.getClass()==v.getClass()){
                        return_tv.setBackgroundColor(Color.parseColor("#ffffff"));
                        Intent intent = new Intent(activity, ReturnActivity.class);
                        startActivity(intent);
                    }
                }
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    // TODO: Rename method, update argument and hook method into UI event

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
