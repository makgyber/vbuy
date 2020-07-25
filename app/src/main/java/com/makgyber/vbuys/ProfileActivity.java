package com.makgyber.vbuys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private final static String COLLECTION = "user";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser mUser = mAuth.getCurrentUser();
    CollectionReference dbRef = db.collection(COLLECTION);

    TextView displayName, email, phoneNumber, address;
    ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        populateProfile();
    }

    private void populateProfile() {
        displayName = findViewById(R.id.tv_display_name);
        phoneNumber = findViewById(R.id.tv_phone_number);
        address = findViewById(R.id.tv_address);
        email = findViewById(R.id.tv_email);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        displayName.setText(sharedPreferences.getString("displayName", "no name"));
        phoneNumber.setText(sharedPreferences.getString("phoneNumber", "no phone"));
        address.setText(sharedPreferences.getString("address", "no address"));
        email.setText(sharedPreferences.getString("email", "no email"));

    }

}