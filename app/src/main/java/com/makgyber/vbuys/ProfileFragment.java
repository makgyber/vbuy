package com.makgyber.vbuys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView displayName, email, phoneNumber, address;
    ImageView profileImage;
    FloatingActionButton updateButton, photoButton;
    String userProfileId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateProfile(view);
    }

    private void populateProfile(View vw) {
        displayName = vw.findViewById(R.id.tv_display_name);
        phoneNumber = vw.findViewById(R.id.tv_phone_number);
        address = vw.findViewById(R.id.tv_address);
        email = vw.findViewById(R.id.tv_email);
        profileImage = vw.findViewById(R.id.iv_profile_photo);
        updateButton = vw.findViewById(R.id.fab_update_profile);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_PROFILE", MODE_PRIVATE);
        userProfileId = sharedPreferences.getString("userId", "");
        displayName.setText(sharedPreferences.getString("displayName", "no name"));
        phoneNumber.setText(sharedPreferences.getString("phoneNumber", "no phone"));
        address.setText(sharedPreferences.getString("address", "no address"));
        email.setText(sharedPreferences.getString("email", "no email"));

        String photoUrl = sharedPreferences.getString("photoUrl", "");
        if (!photoUrl.isEmpty() && photoUrl.toString().length() > 0) {
            Picasso.get().load(photoUrl).centerCrop().resize(400,400).into(profileImage);
        }

        updateButton = vw.findViewById(R.id.fab_update_profile);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
            }
        });

        photoButton = vw.findViewById(R.id.fab_update_photo);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1);
    }
}
