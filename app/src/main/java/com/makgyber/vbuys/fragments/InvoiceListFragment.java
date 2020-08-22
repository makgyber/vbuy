package com.makgyber.vbuys.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.adapters.InvoiceAdapter;
import com.makgyber.vbuys.adapters.InvoiceAdapter;
import com.makgyber.vbuys.models.Invoice;

import static android.content.Context.MODE_PRIVATE;

public class InvoiceListFragment extends Fragment {
    private final String TAG="InvoiceFragment";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference InvoiceRef = db.collection("invoice");
    private InvoiceAdapter adapter;

    public InvoiceListFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment InvoiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvoiceListFragment newInstance() {
        InvoiceListFragment fragment = new InvoiceListFragment();
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
        return inflater.inflate(R.layout.fragment_invoice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getInvoiceList(view);
    }

    private void getInvoiceList(View vw) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");
        Log.d(TAG, "getInvoiceList: UserId - " + userId);
        Query query = InvoiceRef.whereEqualTo("buyerId", userId);

        FirestoreRecyclerOptions<Invoice> options = new FirestoreRecyclerOptions.Builder<Invoice>()
                .setQuery(query, new SnapshotParser<Invoice>() {
                    @NonNull
                    @Override
                    public Invoice parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Invoice Invoice = snapshot.toObject(Invoice.class);
                        Invoice.setId( snapshot.getId() );
                        return Invoice;
                    }
                })
                .build();

        adapter = new InvoiceAdapter(options);
        RecyclerView recyclerView = vw.findViewById(R.id.rv_invoice);
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
