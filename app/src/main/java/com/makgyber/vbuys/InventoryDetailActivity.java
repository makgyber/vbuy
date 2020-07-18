package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryDetailActivity extends AppCompatActivity {
    private final static String TINDAHAN = "tindahan";
    private final static String PRODUCT = "product";
    private final static int PICK_IMAGE = 1;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference tindahanDbRef = db.collection(TINDAHAN);
    CollectionReference productDbRef = db.collection(PRODUCT);

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String productId = "";
    String TAG = "InventoryDetailActivity";
    TextInputEditText name, description, price, tags;
    ImageView productImage;
    Boolean imageUpdated = false;
    ProgressBar spinner;
    String tindahanName, tindahanId, tindahanServiceArea, tindahanContactInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        getSupportActionBar().setTitle("Inventory Details");

        if (getIntent().hasExtra("PRODUCT_ID")) {
            productId = getIntent().getExtras().get("PRODUCT_ID").toString();
            populateProductForm(productId);
        }

        name = (TextInputEditText) findViewById(R.id.name);
        description = (TextInputEditText) findViewById(R.id.description);
        price = (TextInputEditText) findViewById(R.id.price);
        tags = (TextInputEditText) findViewById(R.id.tags);
        productImage = (ImageView) findViewById(R.id.product_image);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE) {
            if (data == null) {
                //return error
                return;
            }
            imageUpdated = true;
            Log.d(TAG, "onActivityResult: " + data.getDataString());
            Uri selectedImage = data.getData();
            productImage.setImageURI(selectedImage);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateProductForm(String productId) {
        DocumentReference docRef = db.collection(PRODUCT).document(productId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.get("productName").toString());
                        description.setText(document.get("description").toString());
                        price.setText(document.get("price").toString());
                        String tagsStr = document.get("tags").toString();
                        tags.setText(tagsStr.replace("[", "").replace("]",""));
                        if (document.get("imageUri") != null && !document.get("imageUri").toString().isEmpty()) {
                            Picasso.get().load(document.get("imageUri").toString()).centerCrop().resize(400,400).into(productImage);
                        }

                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.inventory_add_menu, menu);
        return true;
    }

    private void uploadProductImage() {
        StorageReference productRef = storageRef.child("images/"+tindahanId + "/" + productId + ".jpg");
        productImage.setDrawingCacheEnabled(true);
        productImage.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) productImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.25), (int)(bitmap.getHeight()*0.25), true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();

        spinner.setVisibility(View.VISIBLE);
        UploadTask uploadTask = productRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(InventoryDetailActivity.this, "File Upload failed", Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                updateProductImageUri(downloadUrl);
                Log.d(TAG, "onSuccess: DownLoadUrl " + downloadUrl.toString());
                Toast.makeText(InventoryDetailActivity.this, "File Uploaded" + downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }
        });
    }

    private void updateProductImageUri(Uri downloadUrl) {
        spinner.setVisibility(View.VISIBLE);
        DocumentReference prodRef = productDbRef.document(productId);
        prodRef.update(
                "imageUri", downloadUrl.toString()
        )
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(InventoryDetailActivity.this, "Product ImageUrl updated", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InventoryDetailActivity.this, "Product not updated", Toast.LENGTH_SHORT).show();
                        spinner.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.inventory_save) {
            saveProduct();
            return true;
        }

        if (id == R.id.inventory_camera) {
            Toast.makeText(InventoryDetailActivity.this, "Camera Not yet implemented", Toast.LENGTH_SHORT).show();

            return true;
        }

        if (id == R.id.inventory_gallery) {
            openGallery();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void saveProduct() {
        spinner.setVisibility(View.VISIBLE);
        List<String> tagsList = Arrays.asList(tags.getText().toString().split(","));
        tagsList.replaceAll(String::trim);
        List<String> saList = Arrays.asList(tindahanServiceArea.split(","));
        saList.replaceAll(String::trim);

        Product product = new Product(
                name.getText().toString(),
                description.getText().toString(),
                tindahanName,
                tindahanId,
                Double.parseDouble(price.getText().toString()),
                true, // TODO: use a switch
                tagsList,
                saList,
                "");

        Log.d(TAG, "saveProduct: " + product.getProductName());

        if (productId.equals("0")) {
            DocumentReference pdref = productDbRef.document();
            productId = pdref.getId();
            pdref.set(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            uploadProductImage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            spinner.setVisibility(View.GONE);
                        }
                    });

        } else {
            DocumentReference prodRef = productDbRef.document(productId);
            prodRef.update(
                    "productName", product.getProductName(),
                    "description", product.getDescription(),
                    "price", product.getPrice(),
                    "tags", product.getTags(),
                    "tindahanName", product.getTindahanName()
                    )
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(InventoryDetailActivity.this, "Product updated", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InventoryDetailActivity.this, "Product not updated", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                        }
                    });
            if (imageUpdated)
                uploadProductImage();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getTindahanDetailsFromSharedPreferences();
    }

    private void getTindahanDetailsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("TINDAHAN", MODE_PRIVATE);
        tindahanName = sharedPreferences.getString("tindahanName", "walang tindahan");
        tindahanId = sharedPreferences.getString("tindahanId", "walang laman");
        tindahanContactInfo = sharedPreferences.getString("contactInfo", "");
        tindahanServiceArea = sharedPreferences.getString("serviceArea", "");

        Log.d(TAG, "getTindahanDetailsFromSharedPreferences: " + tindahanName + " " + tindahanServiceArea);
    }
}
