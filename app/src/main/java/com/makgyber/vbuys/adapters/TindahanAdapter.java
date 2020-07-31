package com.makgyber.vbuys.adapters;

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
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.activities.InventoryActivity;
import com.makgyber.vbuys.activities.InventoryDetailActivity;
import com.makgyber.vbuys.activities.StoreSetupActivity;
import com.makgyber.vbuys.models.Product;
import com.makgyber.vbuys.models.Tindahan;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class TindahanAdapter  extends FirestoreRecyclerAdapter<Tindahan, TindahanAdapter.TindahanHolder> {

    public TindahanAdapter(@NonNull FirestoreRecyclerOptions<Tindahan> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TindahanAdapter.TindahanHolder holder, final int position, @NonNull final Tindahan model) {
        Log.d("TAG", "onBindViewHolder: " + model.getId());

        holder.textViewTindahanName.setText(model.getTindahanName());
        holder.tindahanId = model.getId();

        holder.textViewInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tindahanId = model.getId();
                Intent intent = new Intent(v.getContext(), InventoryActivity.class );
                intent.putExtra("TINDAHAN_ID", tindahanId);
                v.getContext().startActivity(intent);
            }
        });

        holder.textViewSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tindahanId = model.getId();
                Intent intent = new Intent(v.getContext(), StoreSetupActivity.class );
                intent.putExtra("TINDAHAN_ID", tindahanId);
                intent.putExtra("OWNER", model.getOwner());
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public TindahanAdapter.TindahanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tindahan_item, parent, false);
        return new TindahanAdapter.TindahanHolder(v);
    }

    class TindahanHolder extends RecyclerView.ViewHolder {
        TextView textViewTindahanName;
        TextView textViewSettings;
        TextView textViewInventory;

        String tindahanId;

        public TindahanHolder(@NonNull final View itemView) {
            super(itemView);
            textViewTindahanName = itemView.findViewById(R.id.tv_tindahan_name);
            textViewSettings = itemView.findViewById(R.id.tv_settings);
            textViewInventory = itemView.findViewById(R.id.tv_inventory);
        }
    }

}