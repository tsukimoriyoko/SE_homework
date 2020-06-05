package com.example.se.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.se.R;
import com.example.se.data.CustomAdapter;
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
        if (parkMetaInfo != null) {
            for (int i = 0; i < parkMetaInfo.size(); i++) {
                JSONObject json = parkMetaInfo.get(i);
                try {
                    String info = json.getString("name") + "停车场，"
                            + "费用：" + (json.getInt("charge") == 0 ? "免费" : "收费");
                    parkInfo.add(info);
                    String info2 = "余位：" + json.getInt("empty_ports") + "/" + json.getInt("total_ports")
                            + "，距离当前位置：";
                    parkInfo_tmp.add(info2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        parkListView = root.findViewById(R.id.parkListView);
        CustomAdapter customAdapter = new CustomAdapter();
        return root;
    }

    private ArrayList<JSONObject> parkMetaInfo = new ArrayList<>();
    private ArrayList<String> parkInfo = new ArrayList<>();
    private ArrayList<String> parkInfo_tmp = new ArrayList<>();
    public ArrayList<String> parkInfo_2 = new ArrayList<>();

    public ListView parkListView;

    public void getGPSLocation() {
        Location gps = LocationUtils.getGPSLocation(this.getContext());
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
