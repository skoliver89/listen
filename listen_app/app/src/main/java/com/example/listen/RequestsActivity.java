package com.example.listen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {
    // ### Class Variables
    private static final String TAG = RequestsActivity.class.getSimpleName();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView.Adapter mAdapter;
    private List<Friend> requestList;

    // ### Custom Methods

    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        requestList = new ArrayList<>();

        // Initialize the RecyclerView Layout and Adapter
        final RecyclerView mRecyclerView = findViewById(R.id.request_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RequestsListAdapter(this, requestList);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data to display (Request Alias)
        CollectionReference requestRef = db.collection("profiles")
                .document(user.getUid())
                .collection("requests");

        requestRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
                    Friend request;
                    String uid;
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()){
                        switch (doc.getType()){
                            case ADDED:
                                // Notify Adapter of new item and add it to the list
                                request = doc.getDocument().toObject(Friend.class);
                                uid = doc.getDocument().getId();
                                request.setUid(uid);
                                requestList.add(request);
                                mAdapter.notifyDataSetChanged();
                                break;
                            case MODIFIED:
                                //TODO Notify adapter of changes
                                break;
                            case REMOVED:
                                // Notify Adapter of removed item and remove it from the list
                                //TODO Make changes without Refresh View
                                request = doc.getDocument().toObject(Friend.class);
                                uid = doc.getDocument().getId();
                                request.setUid(uid);
                                requestList.remove(request);
                                mAdapter.notifyDataSetChanged();
                                // Refresh View
                                finish();
                                startActivity(getIntent());
                                break;
                        }
                    }
            }
        });
    }
}
