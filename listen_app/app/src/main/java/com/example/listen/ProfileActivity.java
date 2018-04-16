package com.example.listen;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends MenuActivity {
    // ### Class Variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private static final String TAG = "ProfileActivity";

    // ### Custom Methods
    private String getData(String uid, final String field){
        String data = "";

        DocumentReference profile = db.collection("profiles").document(uid);

        DocumentSnapshot snap = profile.get().getResult();

        data = snap.getData().get(field).toString();
        return data;
    }
    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView aliasText = findViewById(R.id.profileAlias);
        TextView bioText = findViewById(R.id.profileBio);
        if(user != null){
            String uid = user.getUid();
        }


    }
}