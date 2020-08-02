package com.makgyber.vbuys.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.models.Tindahan;
import java.util.ArrayList;
import java.util.Arrays;

public class StoreSetupActivity extends AppCompatActivity {

    private final static String COLLECTION = "tindahan";
    private final static String TAG = "StoreSetupActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference dbRef = db.collection(COLLECTION);

    TextView mWelcome, mNameHelp, mAddressHelp, mContactInfoHelp, mServiceAreaHelp;
    TextInputEditText mTindahanName, mTindahanId, mOwner, mAddress, mContactInfo, mServiceArea, mDeliveryOptions, mPaymentOptions;
    Boolean storeExists = false;
    String storeId;
    String owner = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setup);
        getSupportActionBar().setTitle("Store Setup");
        mTindahanName = (TextInputEditText) findViewById(R.id.txt_tindahan_name);
        mAddress = (TextInputEditText) findViewById(R.id.txt_address);
        mContactInfo = (TextInputEditText) findViewById(R.id.txt_contact_info);
        mServiceArea = (TextInputEditText) findViewById(R.id.txt_service_area);
        mDeliveryOptions = (TextInputEditText) findViewById(R.id.txt_delivery_mode);
        mPaymentOptions = (TextInputEditText) findViewById(R.id.txt_payment_options);

        populateTindahanView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store_setup_menu, menu);
        return true;
    }

    private void populateTindahanView() {

       if (getIntent().hasExtra("TINDAHAN_ID")) {
           storeId = getIntent().getExtras().get("TINDAHAN_ID").toString();
           dbRef.document(storeId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   if (task.isSuccessful()) {
                       DocumentSnapshot document = task.getResult();
                       if (document.exists()) {
                           storeExists = true;
                           storeId = document.getId();
                           mTindahanName.setText(document.get("tindahanName").toString());
                           mAddress.setText(document.get("address").toString());
                           mContactInfo.setText(document.get("contactInfo").toString());
                           String sArea = document.get("serviceArea").toString();
                           if (document.get("deliveryOptions") != null)
                               mDeliveryOptions.setText(document.get("deliveryOptions").toString());
                           if (document.get("paymentOptions") != null)
                               mPaymentOptions.setText(document.get("paymentOptions").toString());
                           mServiceArea.setText(sArea.replace("[", "").replace("]",""));
                           storeExists = true;
                           saveToSharedPreferences();
                       } else {
                           //no record found,
                           storeExists = false;
                           storeId = dbRef.document().getId();
                       }
                   }
               }
           });
       } else {
           storeExists = false;
           storeId = dbRef.document().getId();
       }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.store_setup_save) {
            saveTindahan();
            return true;
        }

        if (id == R.id.store_setup_refresh) {
            populateTindahanView();
            return true;
        }

        if (id == R.id.action_inventory) {

            if (storeExists) {
                startActivity(new Intent(StoreSetupActivity.this, InventoryActivity.class));
            } else {
                AlertDialog.Builder builder= new AlertDialog.Builder(StoreSetupActivity.this);
                builder.setMessage("Please save your store profile first");
                AlertDialog alert = builder.create();
                alert.show();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveTindahan() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE);
        SharedPreferences sharedUserPreferences = getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        String userId = sharedUserPreferences.getString("userId", "");
        Log.d(TAG, "saveTindahan: userid " + userId);
        String tindahanName = mTindahanName.getText().toString();
        String address = mAddress.getText().toString();
        String contactInfo = mContactInfo.getText().toString();
        String serviceArea = mServiceArea.getText().toString();
        String paymentOptions = mPaymentOptions.getText().toString();
        String deliveryOptions = mDeliveryOptions.getText().toString();
        String owner = userId;

        final Tindahan tindahan = new Tindahan(tindahanName, owner, contactInfo, address,
                paymentOptions, deliveryOptions, true, new ArrayList<String>(Arrays.asList(serviceArea.split(","))));

        dbRef.document(storeId).set(tindahan)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    saveToSharedPreferences();
                    Toast.makeText(StoreSetupActivity.this, "Tindahan saved", Toast.LENGTH_SHORT).show();
                    storeExists = true;
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StoreSetupActivity.this, "Tindahan not saved", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void saveToSharedPreferences() {

        Log.d("StoreSetupActivity", "saveToSharedPreferences: " + COLLECTION + mUser.getUid());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tindahanId", storeId);
        editor.putString("tindahanName", mTindahanName.getText().toString());
        editor.putString("contactInfo", mContactInfo.getText().toString());
        editor.putString("serviceArea", mServiceArea.getText().toString());

        editor.commit();

    }
}
