package com.example.se.ui.home;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.se.data.BillsDataSource;
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
        parking = new BillsDataSource();
    }

    BillsDataSource parking;
    private ArrayList<JSONObject> carportInfo;
    private int parkId;

    // portListView显示的第一行，状态
    private ArrayList<String> portInfo = new ArrayList<>();
    // portListView显示的第二行，距离
    private ArrayList<String> portInfo2 = new ArrayList<>();
    // 记录carportId，用于请求停车/取车
    private ArrayList<Integer> carportId = new ArrayList<>();
    // 记录各个车位是否可用
    private ArrayList<Boolean> isAvailable = new ArrayList<>();

    private void parserPortData() {
        if (carportInfo != null) {
            for (int i = 0; i < carportInfo.size(); i++) {
                JSONObject json = carportInfo.get(i);
                try {
                    String state = json.getString("state");
                    boolean isThisAvailable = state.compareTo("Port_Empty") == 0;
                    int id = Integer.decode(json.getString("id"));
                    String info = "ID：" + id + "    当前状态："
                            + (isThisAvailable ? "空闲" : "占用");
                    double lng1 = json.getDouble("longitude"),
                            lat1 = json.getDouble("latitude"),
                            lng2 = Config.lng, lat2 = Config.lat;
                    double distance = LocationUtils.getDistance(lng1, lng2, lat1, lat2);
                    String info2 = "距离：" + String.format(Locale.CHINA, "%.2f", distance) + "km";
                    Log.d("port_id", "" + id);
                    portInfo.add(info);
                    portInfo2.add(info2);
                    carportId.add(id);
                    isAvailable.add(isThisAvailable);
                } catch (JSONException e) {
                    e.printStackTrace();
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

    boolean isCarportAvailable(int i) {
        return isAvailable.get(i);
    }

    int park(int carportId) {
        return parking.park(carportId);
    }

    int pickup(int carportId) {
        return parking.pickup(carportId);
    }
}
