package com.makgyber.vbuys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("product");
    private ProductAdapter adapter;
    private FirebaseUser user;
    private ImageView ivHealth, ivFood, ivServices, ivRealty;
    private SearchView searchView;
    private SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        setupIconButtons();
    }

    private void setupIconButtons() {
        ivServices = findViewById(R.id.iv_services);
        ivFood = findViewById(R.id.iv_food);
        ivHealth = findViewById(R.id.iv_health);
        ivRealty = findViewById(R.id.iv_realty);

        ivFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManager.triggerSearch("food", getComponentName(), null);
            }
        });

        ivHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManager.triggerSearch("health", getComponentName(), null);
            }
        });

        ivRealty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManager.triggerSearch("realty", getComponentName(), null);
            }
        });

        ivServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchManager.triggerSearch("services", getComponentName(), null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
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

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome ");
        if (user.getDisplayName() != null) {
            sb.append(user.getDisplayName());
        } else if (user.getEmail() != null) {
            sb.append(user.getEmail());
        } else if (user.getPhoneNumber() != null) {
            sb.append(user.getPhoneNumber());
        } else {
            sb.append("guest");
        }

        sb.append("!");
        welcome.setText(sb.toString());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        } else {
            buildMainUi();
        }
    }

    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }


}


