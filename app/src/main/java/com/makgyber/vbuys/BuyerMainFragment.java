package com.makgyber.vbuys;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyerMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerMainFragment extends Fragment {
    private ImageView ivHealth, ivFood, ivServices, ivRealty, ivDevices, ivDelivery;
    private String displayName;
    SearchView searchView;
    SearchManager searchManager;

    public BuyerMainFragment() {
        // Required empty public constructor
    }


    public static BuyerMainFragment newInstance(String displayName) {
        BuyerMainFragment fragment = new BuyerMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        args.putString("displayName", displayName);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            displayName = getArguments().getString("displayName");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buyer_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupIconButtons(view);
    }

    private void setupIconButtons(View view) {
        ivServices = view.findViewById(R.id.iv_services);
        ivFood = view.findViewById(R.id.iv_food);
        ivDevices = view.findViewById(R.id.iv_devices);
        ivDelivery = view.findViewById(R.id.iv_delivery);

        ivFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.searchManager.triggerSearch("food", getActivity().getComponentName(), null);
            }
        });

        ivDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.searchManager.triggerSearch("devices", getActivity().getComponentName(), null);
            }
        });

        ivDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.searchManager.triggerSearch("delivery", getActivity().getComponentName(), null);
            }
        });

        ivServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.searchManager.triggerSearch("services", getActivity().getComponentName(), null);
            }
        });

        TextView welcome = view.findViewById(R.id.text_view_welcome);

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome ");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        String displayName = sharedPreferences.getString("displayName", "");

        sb.append(displayName);
        sb.append("!");
        welcome.setText(sb.toString());
    }

}
