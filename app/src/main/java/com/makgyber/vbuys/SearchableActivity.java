package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SearchableActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference productRef = db.collection("product");
    private SearchProductResultsAdapter adapter;
    private final static String TAG = "SearchableActivity";
    SearchView sv;
    String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        sv = (SearchView) menu.findItem(R.id.search).getActionView();
        sv.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        sv.setIconifiedByDefault(false);

        sv.setQuery(query, false);
        sv.requestFocus();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doMySearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doMySearch(newText);
                return false;
            }
        });


        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "onNewIntent: ");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {

        Log.d(TAG, "doMySearch: " + query);

        Query searchQuery = productRef
                .whereEqualTo("publish", true)
                .whereArrayContains("tags", query.trim());

        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(searchQuery, new SnapshotParser<Product>() {
                    @NonNull
                    @Override
                    public Product parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Product product = snapshot.toObject(Product.class);
                        product.setId( snapshot.getId() );
                        Log.d(TAG, "parseSnapshot: " + snapshot.getId());
                        return product;
                    }
                })
                .build();

        adapter = new SearchProductResultsAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.rv_search_product_result);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchableActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        handleIntent(getIntent());
    }
}


