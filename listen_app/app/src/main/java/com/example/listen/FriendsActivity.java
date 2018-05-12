package com.example.listen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

//References: https://developer.android.com/guide/topics/ui/layout/recyclerview#java
//            https://www.youtube.com/watch?v=kyGVgrLG3KU&t=2s
public class FriendsActivity extends AppCompatActivity {

    // ### Class Variables
    private static final String TAG = FriendsActivity.class.getSimpleName();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Friends> friendsList;

    // ### Custom Methods

    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        friendsList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.friends_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FriendsListAdapter(this,friendsList);
        mRecyclerView.setAdapter(mAdapter);

        // Get the data (aliases)
        CollectionReference friendsRef = db.collection("profiles")
                .document(user.getUid())
                .collection("friends");

        friendsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null){
                    Log.d(TAG, "Error: " + e.getMessage());
                }

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED){
                        Friends friends = doc.getDocument().toObject(Friends.class);
                        friendsList.add(friends);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
