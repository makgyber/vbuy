package com.makgyber.vbuys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("product");
    private CollectionReference userRef = db.collection("user");
    private ProductAdapter adapter;
    private FirebaseUser user;
    private ImageView ivHealth, ivFood, ivServices, ivRealty;
    static SearchView searchView;
    static SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        BottomNavigationView navigationView = findViewById(R.id.bnv_main);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_profile:
                        loadFragment(new ProfileFragment());
                        return true;
                    case R.id.action_home:
                        loadFragment(new BuyerMainFragment());
                        return true;
                    case R.id.action_feedback:
                        return true;
                    case R.id.action_messages:
                        return true;
                }
                return false;
            }
        });
        loadFragment(new BuyerMainFragment());
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
//
//        if (id == R.id.action_profile) {
//            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
//            return true;
//        }

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();

            getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE).edit().clear().commit();
            getApplicationContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE).edit().clear().commit();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            sendToLogin();
        }
    }

    private void sendToLogin() {
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
        finish();
    }

   private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}


