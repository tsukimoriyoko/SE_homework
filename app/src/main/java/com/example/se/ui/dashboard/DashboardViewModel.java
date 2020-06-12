package com.example.se.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.se.data.BillsDataSource;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        BillsDataSource parking = new BillsDataSource();
        int unComplete = parking.getParkedCarport();
        int unPay = parking.getUnpayedBillId();
        if (unComplete == 0 && unPay == 0) {
            mText.setValue("当前没有未完成的订单");
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}