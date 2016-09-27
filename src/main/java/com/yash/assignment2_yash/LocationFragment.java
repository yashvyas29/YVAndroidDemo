package com.yash.assignment2_yash;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;

import static android.R.attr.padding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    private SupportMapFragment mapFragment;
    private ListView listView;
    private ArrayList<MarkerOptions> arrMapMarkers;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
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

        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        createMarkers();

        YVMapMarkersListAdapter listAdapter = new YVMapMarkersListAdapter(getContext(), R.id.markers_list, arrMapMarkers);
        listView = (ListView) view.findViewById(R.id.markers_list);
        listView.setAdapter(listAdapter);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (arrMapMarkers != null) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (Iterator<MarkerOptions> iterator = arrMapMarkers.iterator(); iterator.hasNext(); ) {
                MarkerOptions marker = iterator.next();
                if (marker != null) {
                    googleMap.addMarker(marker);

                    builder.include(marker.getPosition());
                }
            }

            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 15);
            googleMap.animateCamera(cu);
        }
    }

    private void createMarkers() {

        arrMapMarkers = new ArrayList<MarkerOptions>();

        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(28.604739, 77.368915))
                .title("Infogain Noida")
                .snippet("A-16, Sector 60, Noida\n" +
                        "Gautam Budh Nagar,\n" +
                        "201301 (U.P.) India\n" +
                        "Phone: +91 12 0244 5144\n" +
                        "Fax: +91 12 0258 0406");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(37.229715, -121.971905))
                .title("Infogain Corporation, HQ")
                .snippet("485 Alberto Way, Suite 100\n" +
                        "Los Gatos, CA 95032 USA\n" +
                        "Phone: 408-355-6000\n" +
                        "Fax: 408-355-7000");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(33.691409, -117.825765))
                .title("Infogain Irvine")
                .snippet("41 Corporate Park, Suite 390\n" +
                        "Irvine, CA 92606 USA\n" +
                        "Phone: 949-223-5100\n" +
                        "Fax: 949-223-5110");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(30.343444, -97.706788))
                .title("Infogain Austin")
                .snippet("111 W. Anderson Lane, Suite E336\n" +
                        "Austin, Texas 78752\n" +
                        "Phone: 512-212-4070");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(32.944431, -97.113794))
                .title("Infogain Southlake")
                .snippet("1950 State Highway 114, Suite 160\n" +
                        "Southlake, Texas 76902\n" +
                        "Phone: 817-722-5993");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(18.533337, 73.877758))
                .title("Infogain Pune")
                .snippet("Office No. 103 and 103 A, 1st Floor,\n" +
                        "Godrej Castlemaine\n" +
                        "Adjacent To Ruby Hall Clinic,\n" +
                        "Opposite Wadia College,\n" +
                        "Bund Garden Road,\n" +
                        "Pune-411001\n" +
                        "Phone : +91 20 4911 3700");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(19.119613, 72.875878))
                .title("Infogain Mumbai")
                .snippet("8th Floor, The Great Oasis,\n" +
                        "Plot No D-13, MIDC, Marol, \n" +
                        "Andheri (East),\n" +
                        "Mumbai - 400 093\n" +
                        "Phone: +91 22 6695 6969\n" +
                        "Fax: +91 22 6697 3866");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(12.935209, 77.614794))
                .title("Infogain Pune")
                .snippet("#7, 18th Main Road, 7th Block,\n" +
                        "Koramangala,\n" +
                        "Bengaluru - 560 095\n" +
                        "Phone: +91 80 4110 4560\n" +
                        "Fax: +91 80 2552 7563");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(51.491877, -0.125549))
                .title("Infogain London")
                .snippet("Millbank Tower, Citibase \n" +
                        "21-24 Millbank, office no 1.39\n" +
                        "London SW1P 4DP\n" +
                        "Phone: +44 (0)20 3355 7594");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(25.123720, 55.425347))
                .title("Infogain Dubai")
                .snippet("P O Box 500588\n" +
                        "Office No.105, Building No. 4,\n" +
                        "Dubai Outsource Zone,\n" +
                        "Dubai, United Arab Emirates\n" +
                        "Tel: +971-4-458-7336");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(1.278953, 103.849097))
                .title("Infogain Singapore")
                .snippet("Afro-Asia Building\n" +
                        "63 Robinson Road, 07-11, Singapore 068894\n" +
                        "Phone: +65 6274 1455\n" +
                        "Fax: +65 6491 6465");
        arrMapMarkers.add(marker);

        marker = new MarkerOptions()
                .position(new LatLng(3.136635, 101.687442))
                .title("Infogain Malaysia")
                .snippet("Level 27-3 , Q Sentral 2A , Jalan Stesen Sentral 2 \n" +
                        "Kuala Lumpur Sentral ,\n" +
                        "Kuala Lumpur 50470\n" +
                        "Malaysia\n" +
                        "Phone: +60 (3) 2092 9454\n" +
                        "Fax: +60 (3) 2178 4476");
        arrMapMarkers.add(marker);
    }
}
