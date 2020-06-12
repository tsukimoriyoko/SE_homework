package com.example.se.ui.home;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.se.R;
import com.example.se.data.LocationUtils;
import com.example.se.data.Config;
import com.example.se.data.adapter.ParkListAdapter;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

//        final Button getlocation = root.findViewById(R.id.getLocationButton);
//        getlocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getGPSLocation();
//            }
//        });

        carportFragment = root.findViewById(R.id.fragment_carport);
        carportFragment.setVisibility(View.INVISIBLE);

        parkListView = root.findViewById(R.id.parkListView);
        parkListAdapter = new ParkListAdapter(this.getContext(),
                homeViewModel.getParkInfo(), homeViewModel.getParkInfo2());
        parkListView.setAdapter(parkListAdapter);

        parkListView.setOnItemClickListener((parent, view, position, id) -> {
            Config.park_id_to_req = homeViewModel.getParkId((int) id);
            Log.d("park_id", "" + Config.park_id_to_req);
//            root.setVisibility(View.INVISIBLE);
            carportFragment.setVisibility(View.VISIBLE);
            parkListView.setVisibility(View.GONE);
//            FragmentManager fragmentManager = getChildFragmentManager();
//            CarportFragment carportFragment = new CarportFragment();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction
//                    .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                    .add(R.id.fragment_carport, carportFragment)
//                    .show(carportFragment)
//                    .commit();
        });

        Runnable runGPS = this::getGPSLocation;
        Objects.requireNonNull(this.getActivity()).runOnUiThread(runGPS);

        return root;
    }

    View carportFragment;
    private ListView parkListView;
    private ParkListAdapter parkListAdapter;


    public void getGPSLocation() {
        Location gps = LocationUtils.getGPSLocation(Objects.requireNonNull(this.getContext()));
        if (gps == null) {
            LocationUtils.addLocationListener(this.getContext(), LocationManager.GPS_PROVIDER,
                    location -> {
                        if (location != null) {
                            Toast.makeText(HomeFragment.this.getActivity(),
                                    "gps onSuccessLocation location: lat==" + location.getLatitude()
                                            + " lng==" + location.getLongitude(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeFragment.this.getActivity(),
                                    "gps location is null", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            double lng1 = gps.getLongitude(), lat1 = gps.getLatitude();
            Config.lng = lng1;
            Config.lat = lat1;
            homeViewModel.updateParkInfo(lng1, lat1);
            parkListAdapter.updateDistance();
//            Toast.makeText(this.getActivity(), "gps location: lat==" + gps.getLatitude()
//                    + " lng==" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

//    private void getNetworkLocation() {
//        Location net = LocationUtils.getNetWorkLocation(this.getContext());
//        if (net == null) {
//            Toast.makeText(this.getContext(), "net location is null", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this.getContext(), "network location: lat==" + net.getLatitude()
//                    + "  lng==" + net.getLongitude(), Toast.LENGTH_SHORT).show();
//        }
//    }
}
