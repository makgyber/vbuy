package com.makgyber.vbuys.adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.activities.InventoryActivity;
import com.makgyber.vbuys.activities.InventoryDetailActivity;
import com.makgyber.vbuys.activities.MessageActivity;
import com.makgyber.vbuys.activities.StoreSetupActivity;
import com.makgyber.vbuys.fragments.ChatFragment;
import com.makgyber.vbuys.fragments.SellerChatFragment;
import com.makgyber.vbuys.models.Product;
import com.makgyber.vbuys.models.Tindahan;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import static android.content.Context.MODE_PRIVATE;

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
                intent.putExtra("TINDAHAN_NAME", model.getTindahanName());
                intent.putExtra("TINDAHAN_LATITUDE", model.getPosition().getLatitude());
                intent.putExtra("TINDAHAN_LONGITUDE", model.getPosition().getLongitude());
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

        holder.textViewMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("tindahanId", model.getId());
                editor.putString("tindahanName", model.getTindahanName());
                editor.putString("contactInfo", model.getContactInfo());
                editor.putString("tindahanAddress", model.getAddress());
                editor.putString("tindahanLat", Double.toString(model.getPosition().getLatitude()) );
                editor.putString("tindahanLng", Double.toString(model.getPosition().getLongitude()));;
                editor.commit();

                ((FragmentActivity)v.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new SellerChatFragment()).addToBackStack(null).commit();

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
        TextView textViewMessages;

        String tindahanId;

        public TindahanHolder(@NonNull final View itemView) {
            super(itemView);
            textViewTindahanName = itemView.findViewById(R.id.tv_tindahan_name);
            textViewSettings = itemView.findViewById(R.id.tv_settings);
            textViewInventory = itemView.findViewById(R.id.tv_inventory);
            textViewMessages = itemView.findViewById(R.id.tv_messages);
        }
    }

}