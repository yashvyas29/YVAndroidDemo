package com.yash.assignment2_yash;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewPagerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewPagerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_CURRENT_PAGE = "currentPage";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int currentPage;

    private OnFragmentInteractionListener mListener;

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewPagerFragment newInstance(String param1, String param2) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mParam1 = args.getString(ARG_PARAM1);
            mParam2 = args.getString(ARG_PARAM2);
            currentPage = args.getInt(ARG_CURRENT_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.pagerImageView);
        TextView textView = (TextView) view.findViewById(R.id.pagerTextView);

        switch (currentPage) {
            case 0:
                imageView.setImageResource(R.drawable.banner1);
                textView.setText(R.string.view_pager_text1);
                break;
            case 1:
                imageView.setImageResource(R.drawable.banner2);
                textView.setText(R.string.view_pager_text2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.banner3);
                textView.setText(R.string.view_pager_text3);
                break;
            case 3:
                imageView.setImageResource(R.drawable.banner4);
                textView.setText(R.string.view_pager_text4);
                break;
            default:
                imageView.setImageResource(R.drawable.banner1);
                textView.setText(R.string.view_pager_text1);
                break;
        }
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
