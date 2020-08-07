package com.makgyber.vbuys.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.makgyber.vbuys.R;
import com.makgyber.vbuys.adapters.MessageAdapter;
import com.makgyber.vbuys.adapters.ProductAdapter;
import com.makgyber.vbuys.models.Message;
import com.makgyber.vbuys.models.Product;

public class MessageActivity extends AppCompatActivity {

    String chatId, topic;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference messageRef = db.collection("message");
    private MessageAdapter adapter;
    private Button btnSend;
    private EditText edtContent;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (getIntent().hasExtra("chatId")) {
            chatId = getIntent().getExtras().get("chatId").toString();
        }

        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getExtras().get("topic").toString();
        }

        getSupportActionBar().setTitle(topic);

        edtContent = findViewById(R.id.edt_content);

        btnSend = findViewById(R.id.button_chatbox_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage(v);
            }
        });

        getMessageList();
    }

    private void createMessage(View v) {
        if (!edtContent.getText().toString().isEmpty()) {
            Message newMesg = new Message(chatId, Timestamp.now(), "buyer", edtContent.getText().toString());
            messageRef.document().set(newMesg).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    edtContent.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            });
        }

    }

    private void getMessageList() {
        Query query = messageRef.whereEqualTo("chatId", chatId).orderBy("dateCreated", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Message> options = new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, new SnapshotParser<Message>() {
                    @NonNull
                    @Override
                    public Message parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        Message message = snapshot.toObject(Message.class);
                        return message;
                    }
                })
                .build();

        adapter = new MessageAdapter(options);

        recyclerView = findViewById(R.id.rv_messages);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager llman = new LinearLayoutManager(MessageActivity.this);
        recyclerView.setLayoutManager(llman);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}
