package com.makgyber.vbuys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class InventoryDetailActivity extends AppCompatActivity {

    String productId = "";
    String TAG = "InventoryDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        getSupportActionBar().setTitle("Inventory Details");

        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getExtras().get("PRODUCT_ID").toString();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_add_menu, menu);
        return true;
    }
}
