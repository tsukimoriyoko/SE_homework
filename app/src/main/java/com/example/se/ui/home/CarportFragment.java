package com.example.se.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.se.R;
import com.example.se.data.adapter.CarportListAdapter;

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
            Log.d("click", "click port" + id + carportViewModel.getCarportId((int) id));
        });
        return root;
    }

    private ListView carportListView;
    private CarportListAdapter carportListAdapter;
}
