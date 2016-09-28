package com.yash.assignment2_yash;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashboardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    private int pagesCount;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        String[] arrUrls = getResources().getStringArray(R.array.pager_url);
        pagesCount = arrUrls.length;

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        YVFragmentPagerAdapter pagerAdapter = new YVFragmentPagerAdapter(getChildFragmentManager(), pagesCount);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPageChangeListner);

        dotsLayout = (LinearLayout) view.findViewById(R.id.layoutDots);
        addBottomDots(0);

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

    private void addBottomDots(int currentPage) {
        dots = new TextView[pagesCount];

        Resources resources = getResources();
        int[] colorsActive = resources.getIntArray(R.array.array_dot_active);
        int[] colorsInactive = resources.getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(getContext());

            String dotsText;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dotsText = Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY).toString();
            } else {
                dotsText = Html.fromHtml("&#8226;").toString();
            }
            dots[i].setText(dotsText);
            dots[i].setTextSize(35);

            //colorsInactive[currentPage]
            final int version = Build.VERSION.SDK_INT;
            if (version >= 23) {
                dots[i].setTextColor(resources.getColor(R.color.colorInactiveDot, null));
            } else {
                dots[i].setTextColor(resources.getColor(R.color.colorInactiveDot));
            }

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            final int version = Build.VERSION.SDK_INT;
            if (version >= 23) {
                dots[currentPage].setTextColor(resources.getColor(R.color.colorActiveDot, null));
            } else {
                dots[currentPage].setTextColor(resources.getColor(R.color.colorActiveDot));
            }
        }
    }

    // View Pager Change Listner
    ViewPager.OnPageChangeListener viewPageChangeListner = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
