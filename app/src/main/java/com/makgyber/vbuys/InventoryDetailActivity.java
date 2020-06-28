package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    String tindahanName, tindahanId, tindahanServiceArea, tindahanContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        getSupportActionBar().setTitle("Inventory Details");

        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getExtras().get("PRODUCT_ID").toString();
            populateProductForm(productId);
        }

        name = (TextInputEditText) findViewById(R.id.name);
        description = (TextInputEditText) findViewById(R.id.description);
        price = (TextInputEditText) findViewById(R.id.price);
        tags = (TextInputEditText) findViewById(R.id.tags);

    }

    private void populateProductForm(String productId) {
        DocumentReference docRef = db.collection(PRODUCT).document(productId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.get("productName").toString());
                        description.setText(document.get("description").toString());
                        price.setText(document.get("price").toString());
                        String tagsStr = document.get("tags").toString();
                        tags.setText(tagsStr.replace("[", "").replace("]",""));

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.inventory_save) {
            saveProduct();
            return true;
        }

        if (id == R.id.inventory_camera) {
            Toast.makeText(InventoryDetailActivity.this, "Camera Not yet implemented", Toast.LENGTH_SHORT).show();

            return true;
        }

        if (id == R.id.inventory_gallery) {
            Toast.makeText(InventoryDetailActivity.this, "Gallery Not yet implemented", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void saveProduct() {


        Product product = new Product(
                name.getText().toString(),
                description.getText().toString(),
                tindahanName,
                tindahanId,
                Double.parseDouble(price.getText().toString()),
                true, // TODO: use a switch
                new ArrayList<String>(Arrays.asList(tags.getText().toString().split(","))),
                new ArrayList<String>(Arrays.asList(tindahanServiceArea.split(",")))
        );

        Log.d(TAG, "saveProduct: " + product.getProductName());

        if (productId.equals("0")) {

            productDbRef.add(product)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(InventoryDetailActivity.this, "Product saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryDetailActivity.this, "Product not saved", Toast.LENGTH_SHORT).show();
                    }
                });
        } else {
            DocumentReference prodRef = productDbRef.document(productId);
            prodRef.update(
                    "productName", product.getProductName(),
                    "description", product.getDescription(),
                    "price", product.getPrice(),
                    "tags", product.getTags(),
                    "tindahanName", product.getTindahanName()
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InventoryDetailActivity.this, "Product udpated", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InventoryDetailActivity.this, "Product not updated", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getTindahanDetailsFromSharedPreferences();
    }

    private void getTindahanDetailsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE);
        tindahanName = sharedPreferences.getString("tindahanName", "walang tindahan");
        tindahanId = sharedPreferences.getString("tindahanId", "walang laman");
        tindahanContactInfo = sharedPreferences.getString("contactInfo", "");
        tindahanServiceArea = sharedPreferences.getString("serviceArea", "");

        Log.d(TAG, "getTindahanDetailsFromSharedPreferences: " + tindahanName + " " + tindahanServiceArea);
    }
}
