package com.makgyber.vbuys.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makgyber.vbuys.models.Product;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.adapters.ProductAdapter;

public class InventoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("product");
    private ProductAdapter adapter;
    private String storeId = "", tindahanName, tindahanLatitude, tindahanLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_inventory);
        getSupportActionBar().setTitle("Inventory");

        if (getIntent().hasExtra("TINDAHAN_ID")) {
            storeId = getIntent().getExtras().get("TINDAHAN_ID").toString();
        }

        if (getIntent().hasExtra("TINDAHAN_NAME")) {
            tindahanName = getIntent().getExtras().get("TINDAHAN_NAME").toString();
        }

        if (getIntent().hasExtra("TINDAHAN_LATITUDE")) {
            tindahanLatitude = getIntent().getExtras().get("TINDAHAN_LATITUDE").toString();
        }

        if (getIntent().hasExtra("TINDAHAN_LONGITUDE")) {
            tindahanLongitude = getIntent().getExtras().get("TINDAHAN_LONGITUDE").toString();
        }

        getInventoryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inventory_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.inventory_add) {
            Intent intent = new Intent(InventoryActivity.this, InventoryDetailActivity.class);
            intent.putExtra("PRODUCT_ID", "0");
            intent.putExtra("TINDAHAN_ID", storeId);
            intent.putExtra("TINDAHAN_NAME", tindahanName);
            intent.putExtra("TINDAHAN_LATITUDE", tindahanLatitude);
            intent.putExtra("TINDAHAN_LONGITUDE", tindahanLongitude);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getInventoryList() {
        Query query = productRef.whereEqualTo("tindahanId", storeId);

        Log.d("InventoryActivity", "getInventoryList: STORE: " + storeId);
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query, new SnapshotParser<Product>() {
                    @NonNull
                    @Override
                    public Product parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Product product = snapshot.toObject(Product.class);
                        product.setId( snapshot.getId() );
                        return product;
                    }
                })
                .build();

        adapter = new ProductAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(InventoryActivity.this));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
