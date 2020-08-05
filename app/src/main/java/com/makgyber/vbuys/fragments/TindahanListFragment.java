package com.makgyber.vbuys.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.activities.InventoryActivity;
import com.makgyber.vbuys.activities.StoreSetupActivity;
import com.makgyber.vbuys.adapters.ProductAdapter;
import com.makgyber.vbuys.adapters.TindahanAdapter;
import com.makgyber.vbuys.models.Product;
import com.makgyber.vbuys.models.Tindahan;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TindahanListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TindahanListFragment extends Fragment {

    private final String TAG="TindahanListFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference tindahanRef = db.collection("tindahan");
    private TindahanAdapter adapter;
    ExtendedFloatingActionButton addTindahan;

    public TindahanListFragment() {
        // Required empty public constructor
    }

    public static TindahanListFragment newInstance() {
        TindahanListFragment fragment = new TindahanListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tindahan_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addTindahan = view.findViewById(R.id.efab_add_tindahan);
        addTindahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StoreSetupActivity.class));

            }
        });
        getInventoryList(view);
    }

    private void getInventoryList(View vw) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Log.d(TAG, "getInventoryList: UserId - " + userId);
        Query query = tindahanRef.whereEqualTo("owner", userId);

        FirestoreRecyclerOptions<Tindahan> options = new FirestoreRecyclerOptions.Builder<Tindahan>()
                .setQuery(query, new SnapshotParser<Tindahan>() {
                    @NonNull
                    @Override
                    public Tindahan parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Tindahan tindahan = snapshot.toObject(Tindahan.class);
                        Log.d(TAG, "getInventoryList: StoreId - " + snapshot.getId() );
                        tindahan.setId( snapshot.getId() );
                        return tindahan;
                    }
                })
                .build();

        adapter = new TindahanAdapter(options);

        RecyclerView recyclerView = vw.findViewById(R.id.rv_tindahan);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
