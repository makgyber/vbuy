package com.makgyber.vbuys.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.models.Chat;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {

    private final static String COLLECTION = "tindahan";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference dbRef = db.collection(COLLECTION);
    CollectionReference chatRef = db.collection("chat");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    private String productId;
    private String productName;
    private String productDescription;
    private String productImageUri;
    private String productPrice;
    private String tindahanId, tindahanName;

    private static String TAG = "ProductDetailActivity";

    TextView tvProductName, tvDescription, tvPrice, tvTindahanName, tvDeliveryOptions, tvPaymentOptions, tvContactInfo;
    ImageView ivProduct;
    Button bMessageSeller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        setupPage();
    }

    private void setupPage() {
        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getExtras().get("PRODUCT_ID").toString();
        }

        if (getIntent().hasExtra("PRODUCT_NAME")) {
            productName = getIntent().getExtras().get("PRODUCT_NAME").toString();
            getSupportActionBar().setTitle(productName);
        }

        if (getIntent().hasExtra("PRODUCT_DESCRIPTION")) {
            productDescription = getIntent().getExtras().get("PRODUCT_DESCRIPTION").toString();
        }

        if (getIntent().hasExtra("PRODUCT_IMAGE")) {
            productImageUri = getIntent().getExtras().get("PRODUCT_IMAGE").toString();
        }

        if (getIntent().hasExtra("PRODUCT_PRICE")) {
            productPrice = getIntent().getExtras().get("PRODUCT_PRICE").toString();
        }

        if (getIntent().hasExtra("PRODUCT_TINDAHAN_ID")) {
            tindahanId = getIntent().getExtras().get("PRODUCT_TINDAHAN_ID").toString();
        }

        if (getIntent().hasExtra("PRODUCT_TINDAHAN_NAME")) {
            tindahanName = getIntent().getExtras().get("PRODUCT_TINDAHAN_NAME").toString();
        }

        Log.d(TAG, "onCreate: PRODUCT_ID " + productId);

        tvProductName = findViewById(R.id.product_detail_name);
        tvDescription = findViewById(R.id.product_detail_description);
        tvPrice = findViewById(R.id.product_detail_price);
        ivProduct = findViewById(R.id.product_detail_image);

        tvTindahanName = findViewById(R.id.product_detail_tindahan_name);
        tvDeliveryOptions = findViewById(R.id.delivery_options);
        tvPaymentOptions = findViewById(R.id.payment_options);
        tvContactInfo = findViewById(R.id.contact_info);

        tvProductName.setText(productName);
        tvDescription.setText(productDescription);
        tvPrice.setText("Php " + productPrice);
        Picasso.get().load(productImageUri).centerCrop().into(ivProduct);

        bMessageSeller = findViewById(R.id.btn_message_seller);
        bMessageSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChatSession();
            }
        });

        populateTindahan();

    }

    private void createChatSession() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        String userProfileId = sharedPreferences.getString("userId", "");
        String displayName = sharedPreferences.getString("displayName", "no name");
        String photoUrl = sharedPreferences.getString("photoUrl", "");

        Log.d(TAG, "createChatSession: tindahanId " + tindahanId);
        Log.d(TAG, "createChatSession: tindahanName " + tindahanName);

        DocumentReference docRef = chatRef.document();
        String newId = chatRef.document().getId();
        Chat chat = new Chat(newId, productName, userProfileId, displayName, photoUrl, tindahanId, tindahanName, photoUrl, Timestamp.now());

        docRef.set(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "New session created", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                intent.putExtra("chatId", newId);
                intent.putExtra("topic", productName);
                startActivity(intent);
            }
        });
    }

    private void populateTindahan() {
        Log.d(TAG, "populateTindahan: tindahanId " + tindahanId);
        final DocumentReference docRef = db.collection(COLLECTION).document(tindahanId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvTindahanName.setText("Sold by: " + document.get("tindahanName").toString());
                        tvContactInfo.setText("Contact Info:\n" + document.get("contactInfo").toString());

                        if (document.get("deliveryOptions") != null)
                            tvDeliveryOptions.setText("Delivery Options:\n" + document.get("deliveryOptions").toString());
                        if (document.get("paymentOptions") != null)
                            tvPaymentOptions.setText("Payment Options:\n" +document.get("paymentOptions").toString());
                    }
                }
            }
        });
    }
}
