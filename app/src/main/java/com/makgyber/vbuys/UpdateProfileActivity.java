package com.makgyber.vbuys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateProfileActivity extends AppCompatActivity {

    private final static String COLLECTION = "user";
    private static final String TAG = "UpdateProfileActivity";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDbRef = db.collection(COLLECTION);
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();

    TextInputEditText displayName, phoneNumber, email, address;
    ImageView profileImage;
    FloatingActionButton saveProfileButton;
    String userProfileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        populateUpdateForm();

        saveProfileButton = findViewById(R.id.fab_save_profile);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileToDB();
            }
        });
    }

    private void saveProfileToDB() {

        DocumentReference userRef = userDbRef.document(userProfileId);
        userRef.update(
                "displayName", displayName.getText().toString(),
                "email", email.getText().toString(),
                "phoneNumber", phoneNumber.getText().toString(),
                "address", address.getText().toString()
        )
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(UpdateProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                saveToSharedPreferences();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateProfileActivity.this, "Profile not updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateUpdateForm() {
        displayName = findViewById(R.id.tiet_display_name);
        phoneNumber = findViewById(R.id.tiet_phone_number);
        email = findViewById(R.id.tiet_email);
        address = findViewById(R.id.tiet_address);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        userProfileId = sharedPreferences.getString("userId", "");
        displayName.setText(sharedPreferences.getString("displayName", "no name"));
        phoneNumber.setText(sharedPreferences.getString("phoneNumber", "no phone"));
        address.setText(sharedPreferences.getString("address", "no address"));
        email.setText(sharedPreferences.getString("email", "no email"));
    }

    private void saveToSharedPreferences() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email.getText().toString());
        editor.putString("displayName", displayName.getText().toString());
        editor.putString("phoneNumber", phoneNumber.getText().toString());
        editor.putString("address", address.getText().toString());
        editor.commit();
    }


}
