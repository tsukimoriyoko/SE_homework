package com.example.se.ui.home;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.se.data.LocationUtils;
import com.example.se.data.Park;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
        mText.setValue("");

        Park park = new Park();
        parkMetaInfo = park.getParks();
        parserMetaInfo();
    }

    // /MetaInfo请求到的JSON数据
    private ArrayList<JSONObject> parkMetaInfo = new ArrayList<>();
    // 显示的第一行，名称、费用
    private ArrayList<String> parkInfo = new ArrayList<>();
    // 用于生成显示的第二行，包括余位，不包括距离
    private ArrayList<String> parkInfo_tmp = new ArrayList<>();
    // 显示的第二行，余位、距离
    private ArrayList<String> parkInfo_2 = new ArrayList<>();
    // 各个停车场的位置
    private ArrayList<Pair<Double, Double>> parkLocation = new ArrayList<>();
    // 到各个停车场的距离
    private ArrayList<Double> parkDistance = new ArrayList<>();
    // 停车场的id，用于请求/ParkInfo
    private ArrayList<Integer> parkId = new ArrayList<>();

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


    LiveData<String> getText() {
        return mText;
    }

    ArrayList<String> getParkInfo() {
        return parkInfo;
    }

    ArrayList<String> getParkInfo_2() {
        return parkInfo_2;
    }

    int getParkId(int id) {
        return parkId.get(id);
    }

    void updateParkInfo(double lng1, double lat1) {
        for (int i = 0; i < parkLocation.size(); i++) {
            double lng2 = parkLocation.get(i).first,
                    lat2 = parkLocation.get(i).second;
            parkDistance.set(i, LocationUtils.getDistance(lng1, lng2, lat1, lat2));
            parkInfo_2.set(i, parkInfo_tmp.get(i)
                    + String.format(Locale.CHINA, "%.2f", parkDistance.get(i)) + "km");
            Log.d("distance", parkInfo_2.get(i));
        }
    }
}