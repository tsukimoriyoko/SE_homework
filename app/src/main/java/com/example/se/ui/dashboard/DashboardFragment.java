package com.example.se.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.se.R;

import java.util.Objects;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        AlertDialog.Builder pickupBuilder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        pickupBuilder.setMessage("是否确定取车？")
                .setIcon(R.drawable.pay)
                .setPositiveButton("确定", (dialog, id) -> {
                    // User ensure the dialog
                    if (dashboardViewModel.pickup(carportId) == 0) {
                        Toast.makeText(DashboardFragment.this.getActivity(),
                                "取车成功", Toast.LENGTH_SHORT).show();
                        pickupButton.setVisibility(View.INVISIBLE);
                        payButton.setVisibility(View.VISIBLE);
                        dashboardViewModel.updateUserInfo();
//                        textView.setText(dashboardViewModel.getmText());
                    } else {
                        Toast.makeText(DashboardFragment.this.getActivity(),
                                "取车失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, id) -> {
                    // User cancelled the dialog
                });

        AlertDialog.Builder payBuilder = new AlertDialog.Builder(getActivity());
        payBuilder.setMessage("是否确定付款？")
                .setIcon(R.drawable.pay)
                .setPositiveButton("确定", (dialog, id) -> {
                    // User ensure the dialog
                    if (dashboardViewModel.pay(billId) == 0) {
                        Toast.makeText(DashboardFragment.this.getActivity(),
                                "付款成功", Toast.LENGTH_SHORT).show();
                        payButton.setVisibility(View.INVISIBLE);
                        pickupButton.setVisibility(View.INVISIBLE);
                        dashboardViewModel.updateUserInfo();
//                        textView.setText(dashboardViewModel.getmText());
                    } else {
                        Toast.makeText(DashboardFragment.this.getActivity(),
                                "付款失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消", (dialog, id) -> {
                    // User cancelled the dialog
                });

        pickupButton = root.findViewById(R.id.button_pickup);
        payButton = root.findViewById(R.id.button_pay);

        carportId = dashboardViewModel.isUnComplete();
        if (carportId > 0) {
            pickupButton.setVisibility(View.VISIBLE);
            payButton.setVisibility(View.INVISIBLE);
        }
        pickupButton.setOnClickListener(v -> {
            pickupBuilder.create();
            pickupBuilder.show();
        });

        billId = dashboardViewModel.isUnPay();
        if (billId > 0) {
            payButton.setVisibility(View.VISIBLE);
            pickupButton.setVisibility(View.INVISIBLE);
        }
        payButton.setOnClickListener(v -> {
            payBuilder.create();
            payBuilder.show();
        });
        return root;
    }

    private Button pickupButton, payButton;
    private int carportId, billId;
}
