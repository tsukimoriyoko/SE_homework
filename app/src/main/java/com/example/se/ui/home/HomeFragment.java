package com.example.se.ui.home;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Pair;
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
import com.example.se.data.Carport;
import com.example.se.data.adapter.ParkListAdapter;
import com.example.se.data.LocationUtils;
import com.example.se.data.Park;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        Park park = new Park();
        parkMetaInfo = park.getParks();
        parserMetaInfo();

        parkListView = root.findViewById(R.id.parkListView);
        parkListAdapter = new ParkListAdapter(this.getContext(), parkInfo, parkInfo_2);
        parkListView.setAdapter(parkListAdapter);

        parkListView.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<JSONObject> carportJson = Carport.getCarport(parkId.get((int) id));
        });

        Runnable runGPS = this::getGPSLocation;
        this.getActivity().runOnUiThread(runGPS);

        return root;
    }

    private ArrayList<JSONObject> parkMetaInfo = new ArrayList<>();
    private ArrayList<String> parkInfo = new ArrayList<>();
    private ArrayList<String> parkInfo_tmp = new ArrayList<>();
    private ArrayList<String> parkInfo_2 = new ArrayList<>();
    private ArrayList<Pair<Double, Double>> parkLocation = new ArrayList<>();
    private ArrayList<Double> parkDistance = new ArrayList<>();
    private ArrayList<Integer> parkId = new ArrayList<>();

    private ListView parkListView;
    private ParkListAdapter parkListAdapter;

    private void parserMetaInfo() {
        if (parkMetaInfo != null) {
            for (int i = 0; i < parkMetaInfo.size(); i++) {
                JSONObject json = parkMetaInfo.get(i);
                try {
                    String info = json.getString("name") + "停车场，"
                            + "费用：" + (json.getInt("charge") == 0 ? "免费" : "收费");
                    parkInfo.add(info);
                    String info2 = "余位：" + json.getInt("empty_ports") + "/"
                            + json.getInt("total_ports") + "，距离当前位置：";
                    parkInfo_tmp.add(info2);
                    Pair<Double, Double> location = new Pair<>(json.getDouble("longitude"),
                            json.getDouble("latitude"));
                    parkLocation.add(location);
                    parkDistance.add(-1.0);
                    parkInfo_2.add(info2 + "-1");
                    parkId.add(Integer.getInteger(json.getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void getGPSLocation() {
        Location gps = LocationUtils.getGPSLocation(this.getContext());
        if (gps == null) {
            LocationUtils.addLocationListener(this.getContext(), LocationManager.GPS_PROVIDER,
                    location -> {
                        if (location != null) {
                            double lng1 = location.getLongitude(), lat1 = location.getLatitude();
                            for (int i = 0; i < parkLocation.size(); i++) {
                                double lng2 = parkLocation.get(i).first,
                                        lat2 = parkLocation.get(i).second;
                                parkDistance.set(i, LocationUtils.getDistance(lng1, lng2, lat1, lat2));
                                parkInfo_2.set(i, parkInfo_tmp.get(i) + parkDistance.get(i) + "km");
                                parkListAdapter.updateDistance(parkInfo_2);
                            }
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
            Toast.makeText(this.getActivity(), "gps location: lat==" + gps.getLatitude()
                    + " lng==" + gps.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getNetworkLocation() {
        Location net = LocationUtils.getNetWorkLocation(this.getContext());
        if (net == null) {
            Toast.makeText(this.getContext(), "net location is null", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.getContext(), "network location: lat==" + net.getLatitude()
                    + "  lng==" + net.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }
}
