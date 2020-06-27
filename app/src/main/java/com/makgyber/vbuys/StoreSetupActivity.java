package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;

public class StoreSetupActivity extends AppCompatActivity {

    private final static String COLLECTION = "tindahan";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference dbRef = db.collection(COLLECTION);

    TextView mWelcome, mNameHelp, mAddressHelp, mContactInfoHelp, mServiceAreaHelp;
    EditText mTindahanName, mTindahanId, mOwner, mAddress, mContactInfo, mServiceArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setup);
        getSupportActionBar().setTitle("Store Setup");
        mTindahanName = (EditText) findViewById(R.id.txt_tindahan_name);
        mAddress = (EditText) findViewById(R.id.txt_address);
        mContactInfo = (EditText) findViewById(R.id.txt_contact_info);
        mServiceArea = (EditText) findViewById(R.id.txt_service_area);

        generateLabels();
        populateTindahanView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.store_setup_menu, menu);
        return true;
    }

    private void generateLabels() {
        mWelcome = (TextView) findViewById(R.id.txt_tindahan_welcome);
        mNameHelp = (TextView) findViewById(R.id.txt_tindahan_name_help);
        mAddressHelp = (TextView) findViewById(R.id.txt_address_help);
        mContactInfoHelp = (TextView) findViewById(R.id.txt_contact_info_help);
        mServiceAreaHelp = (TextView) findViewById(R.id.txt_service_area_help);

        mWelcome.setText(Html.fromHtml(mWelcome.getText().toString()));
        mNameHelp.setText(Html.fromHtml(mNameHelp.getText().toString()));
        mAddressHelp.setText(Html.fromHtml(mAddressHelp.getText().toString()));
        mContactInfoHelp.setText(Html.fromHtml(mContactInfoHelp.getText().toString()));
        mServiceAreaHelp.setText(Html.fromHtml(mServiceAreaHelp.getText().toString()));
    }

    private void populateTindahanView() {

        DocumentReference docRef = db.collection(COLLECTION).document(COLLECTION + mUser.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        mTindahanName.setText(document.get("tindahanName").toString());
                        mAddress.setText(document.get("address").toString());
                        mContactInfo.setText(document.get("contactInfo").toString());
                        String sArea = document.get("serviceArea").toString();
                        mServiceArea.setText(sArea.replace("[", "").replace("]",""));
                    }
                }
            }
        });
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

            if (mTindahanName.toString().equalsIgnoreCase("")) {
                AlertDialog.Builder builder= new AlertDialog.Builder(StoreSetupActivity.this);
                builder.setMessage("Please save your store profile first");
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                startActivity(new Intent(StoreSetupActivity.this, InventoryActivity.class));
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveTindahan() {
        String tindahanName = mTindahanName.getText().toString();
        String address = mAddress.getText().toString();
        String contactInfo = mContactInfo.getText().toString();
        String serviceArea = mServiceArea.getText().toString();
        String owner = mUser.getUid();
        String tindahanId = COLLECTION + owner;

        Tindahan tindahan = new Tindahan(tindahanId, tindahanName, owner, contactInfo, address,
                true, new ArrayList<String>(Arrays.asList(serviceArea.split(","))));

        dbRef.document(tindahanId).set(tindahan)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(StoreSetupActivity.this, "Tindahan saved", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StoreSetupActivity.this, "Tindahan not saved", Toast.LENGTH_SHORT).show();
                }
            });
    }

}
