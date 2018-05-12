package com.example.listen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    // ### Class Variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = FriendsActivity.class.getSimpleName();



    // ### Custom Methods


    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        String uid = user.getUid();
        final List<String> fAlias = new ArrayList<>();
        final List<String> fUID = new ArrayList<>();
        CollectionReference friendsRef = db.collection("profile")
                .document(uid)
                .collection("friends");

        friendsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult()){
                        // Populate the Friends list
                        fAlias.add(document.get("alias").toString());
                        fUID.add(document.get("friendUID").toString());
                    }
                }
                else { Log.d(TAG, "Error getting Friends List: ", task.getException()); }
            }
        });
        for (String alias : fAlias){
            Log.d(TAG, "Friend Alias: " + alias);
        }

    }
}
