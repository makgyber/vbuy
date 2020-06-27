package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryDetailActivity extends AppCompatActivity {
    private final static String TINDAHAN = "tindahan";
    private final static String PRODUCT = "product";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference tindahanDbRef = db.collection(TINDAHAN);
    CollectionReference productDbRef = db.collection(PRODUCT);

    String productId = "";
    String TAG = "InventoryDetailActivity";
    TextInputEditText name, description, price, tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        getSupportActionBar().setTitle("Inventory Details");

        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getExtras().get("PRODUCT_ID").toString();
        }

        name = (TextInputEditText) findViewById(R.id.name);
        description = (TextInputEditText) findViewById(R.id.description);
        price = (TextInputEditText) findViewById(R.id.price);
        tags = (TextInputEditText) findViewById(R.id.tags);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.inventory_add) {
            saveProduct();
            return true;
        }

        if (id == R.id.inventory_save) {

            return true;
        }

        if (id == R.id.inventory_camera) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {


        Product product = new Product(
                name.getText().toString(),
                description.getText().toString(),
                "tindahanName",
                "tindahanId",
                Double.parseDouble(price.getText().toString()),
                true,
                new ArrayList<String>(Arrays.asList(tags.getText().toString().split(","))),
                new ArrayList<String>(Arrays.asList(tags.getText().toString().split(",")))
        );


    }
}
