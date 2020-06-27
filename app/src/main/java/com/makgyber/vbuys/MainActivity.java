package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("product");
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        buildMainUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, StoreSetupActivity.class));
            return true;
        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void buildMainUi() {
        TextView welcome = findViewById(R.id.text_view_welcome);
        if (!mAuth.getCurrentUser().getDisplayName().isEmpty()) {
            welcome.setText("Welcome " + mAuth.getCurrentUser().getDisplayName() + "!");
        } else if (!mAuth.getCurrentUser().getEmail().isEmpty()) {
            welcome.setText("Welcome " + mAuth.getCurrentUser().getEmail() + "!");
        } else if (!mAuth.getCurrentUser().getPhoneNumber().isEmpty()) {
            welcome.setText("Welcome " + mAuth.getCurrentUser().getPhoneNumber() + "!");
        } else {
            welcome.setText("Welcome stranger!");
        }

//        Query query = productRef.whereEqualTo("publish", true)
//                .whereEqualTo("tindahanId", "tindahan" + mAuth.getCurrentUser().getUid());
//
//        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
//                .setQuery(query, Product.class)
//                .build();
//
//        adapter = new ProductAdapter(options);
//
//        RecyclerView recyclerView = findViewById(R.id.rv_products);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStop() {
        super.onStop();
//        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        }
    }

    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}

    //    @Override
//    public void onBackPressed() {
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setMessage("Exit the application?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//
//    }

