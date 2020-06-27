package com.makgyber.vbuys;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {

    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Product model) {
        holder.textViewProductName.setText(model.getProductName());
        holder.textViewProductDescription.setText(model.getDescription());
        holder.textViewProductPrice.setText(Double.toString(model.getPrice()));
        holder.textViewTindahan.setText(model.getTindahanName());
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductHolder(v);
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        TextView textViewProductName;
        TextView textViewProductDescription;
        TextView textViewProductPrice;
        TextView textViewTindahan;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.txt_product_name);
            textViewProductDescription = itemView.findViewById(R.id.txt_product_description);
            textViewProductPrice = itemView.findViewById(R.id.txt_product_price);
            textViewTindahan = itemView.findViewById(R.id.txt_tindahan);
        }
    }

}
