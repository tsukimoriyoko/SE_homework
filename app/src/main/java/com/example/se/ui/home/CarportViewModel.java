package com.example.se.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.se.data.CarportDataSource;
import com.example.se.data.Config;
import com.example.se.data.LocationUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class CarportViewModel extends ViewModel {
    public CarportViewModel() {
        parkId = Config.park_id_to_req;
        carportInfo = CarportDataSource.getCarport(parkId);
//        Log.d("xxx", "got carport info");
        parserPortData();
    }

    ArrayList<JSONObject> carportInfo;
    int parkId;

    // portListView显示的第一行，状态
    private ArrayList<String> portInfo = new ArrayList<>();
    // portListView显示的第二行，距离
    private ArrayList<String> portInfo2 = new ArrayList<>();
    // 记录carportId，用于请求停车/取车
    private ArrayList<Integer> carportId = new ArrayList<>();

    private void parserPortData() {
        if (carportInfo != null) {
            for (int i = 0; i < carportInfo.size(); i++) {
                JSONObject json = carportInfo.get(i);
                try {
                    String state = json.getString("state");
                    int id = Integer.decode(json.getString("id"));
                    String info = "ID：" + id + "    当前状态："
                            + (state.compareTo("Port_Empty") == 0 ? "空闲" : "占用");
                    double lng1 = json.getDouble("longitude"),
                            lat1 = json.getDouble("latitude"),
                            lng2 = Config.lng, lat2 = Config.lat;
                    double distance = LocationUtils.getDistance(lng1, lng2, lat1, lat2);
                    String info2 = "距离：" + String.format(Locale.CHINA, "%.2f", distance) + "km";
                    Log.d("port_id", "" + id);
                    portInfo.add(info);
                    portInfo2.add(info2);
                    carportId.add(id);
                } catch (JSONException e) {
                    e.printStackTrace();
                    ;
                }
            }
        }
    }

    ArrayList<String> getPortInfo() {
        return portInfo;
    }

    ArrayList<String> getPortInfo2() {
        return portInfo2;
    }

    int getCarportId(int i) {
        return carportId.get(i);
    }
}
