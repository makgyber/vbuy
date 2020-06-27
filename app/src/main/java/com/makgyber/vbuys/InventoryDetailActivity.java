package com.makgyber.vbuys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class InventoryDetailActivity extends AppCompatActivity {

    String productId;
    String TAG = "InventoryDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        getSupportActionBar().setTitle("Inventory Details");

        String productId = getIntent().getExtras().get("PRODUCT_ID").toString();
        Log.d(TAG, "onCreate: " + productId);
    }
}
