package com.example.se.ui.dashboard;

import android.icu.text.SimpleDateFormat;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.se.data.BillsDataSource;
import com.example.se.data.Config;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();

        parking = new BillsDataSource();
        unComplete = parking.getParkedCarport();
        unPay = parking.getUnpayedBillId();

        if (unComplete == 0 && unPay == 0) {
            mText.setValue("当前没有未完成的订单");
        } else if (unComplete > 0) {
            String info = "当前有未完成订单，停车位：" + unComplete + "\n开始时间：" + Config.parkedTime;
            mText.setValue(info);
        } else if (unPay > 0) {
            String info = "有尚未支付的订单，订单号：" + unPay;
            mText.setValue(info);
        }
    }

    BillsDataSource parking;
    private int unComplete;
    private int unPay;

    int isUnComplete() {
        return unComplete;
    }

    int isUnPay() {
        return unPay;
    }

    LiveData<String> getText() {
        return mText;
    }

    String getmText() {
        return mText.getValue();
    }

    void setText(String text) {
        mText.setValue(text);
    }

    void updateUserInfo() {
        unComplete = parking.getParkedCarport();
        unPay = parking.getUnpayedBillId();

        if (unComplete == 0 && unPay == 0) {
            mText.setValue("当前没有未完成的订单");
        } else if (unComplete > 0) {
            String info = "当前有进行中的订单，停车位：" + unComplete + "\n开始时间：" + Config.parkedTime;
            mText.setValue(info);
        } else if (unPay > 0) {
            String info = "当前有尚未支付的订单，订单号：" + unPay;
            mText.setValue(info);
        }
    }

    int pickup(int carportId) {
        return parking.pickup(carportId);
    }

    int pay(int billId) {
        return parking.pay(billId);
    }
}