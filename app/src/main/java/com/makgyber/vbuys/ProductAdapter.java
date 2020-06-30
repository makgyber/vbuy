package com.makgyber.vbuys;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product, ProductAdapter.ProductHolder> {

    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, final int position, @NonNull final Product model) {
        Log.d("TAG", "onBindViewHolder: " + model.getId());

        holder.textViewProductName.setText(model.getProductName());
        holder.textViewProductDescription.setText(model.getDescription());
        holder.textViewProductPrice.setText(Double.toString(model.getPrice()));
        holder.textViewTindahan.setText(model.getTindahanName());
        holder.productId = model.getId();
        if (model.getImageUri() != null && !model.getImageUri().toString().isEmpty()) {
            Picasso.get().load(model.getImageUri().toString()).centerCrop().resize(200,200).into(holder.productImage);
            Log.d("PRODUCT ADAPTER", "onBindViewHolder: " + model.getImageUri().toString());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String product_id = model.getId();
                Intent intent = new Intent(v.getContext(), InventoryDetailActivity.class );
                intent.putExtra("PRODUCT_ID", product_id);
                v.getContext().startActivity(intent);
            }
        });
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
        String productId;
        ImageView productImage;

        public ProductHolder(@NonNull final View itemView) {
            super(itemView);
            textViewProductName = itemView.findViewById(R.id.txt_product_name);
            textViewProductDescription = itemView.findViewById(R.id.txt_product_description);
            textViewProductPrice = itemView.findViewById(R.id.txt_product_price);
            textViewTindahan = itemView.findViewById(R.id.txt_tindahan);
            productImage = itemView.findViewById(R.id.iv_product_image);
        }
    }

}
