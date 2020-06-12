package com.example.se.ui.home;

import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.se.R;
import com.example.se.data.BillsDataSource;
import com.example.se.data.Config;
import com.example.se.data.adapter.CarportListAdapter;

import java.util.Objects;

public class CarportFragment extends Fragment {
    private CarportViewModel carportViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        carportViewModel =
                ViewModelProviders.of(this).get(CarportViewModel.class);
        View root = inflater.inflate(R.layout.fragment_carport, container, false);

        carportListView = root.findViewById(R.id.portListView);
        carportListAdapter = new CarportListAdapter(this.getContext(),
                carportViewModel.getPortInfo(), carportViewModel.getPortInfo2());
        carportListView.setAdapter(carportListAdapter);

        carportListView.setOnItemClickListener((parent, view, position, id) -> {
            int carportId = carportViewModel.getCarportId((int) id);
            int itemId = (int) id;
            Log.d("click", "click port" + id + " " + carportId);
            if (!carportViewModel.isCarportAvailable(itemId)) {
                Toast.makeText(CarportFragment.this.getActivity(),
                        "该车位已被占用", Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setMessage("是否确定停车？")
                    .setIcon(R.drawable.pay)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User ensure the dialog
                            if (carportViewModel.park(carportId) == 0) {
                                Toast.makeText(CarportFragment.this.getActivity(),
                                        "停车成功", Toast.LENGTH_SHORT).show();
                                long currentTime = System.currentTimeMillis();
                                Config.parkedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(currentTime);
                            } else {
                                Toast.makeText(CarportFragment.this.getActivity(),
                                        "停车失败，已有尚未完成的订单", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            builder.create();
            builder.show();
        });
        return root;
    }

    private ListView carportListView;
    private CarportListAdapter carportListAdapter;
}
