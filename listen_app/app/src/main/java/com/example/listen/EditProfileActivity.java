package com.example.listen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {
    // ### Class Variables
    // For the logs
    private static final String TAG = "EditProfileActivity";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final EditText alias = findViewById(R.id.editProfileAlias);
        final EditText bio = findViewById(R.id.editProfileBio);
        if (user != null){
            final String uid = user.getUid();
            DocumentReference profRef = db.collection("profiles").document(uid);
            profRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot profile = task.getResult();
                        if (profile.exists()){
                            alias.setHint(profile.getString("alias"));
                            bio.setHint(profile.getString("bio"));
                        }
                        else { Log.d(TAG, "No Profile found for user: " + uid); }
                    }
                    else { Log.d(TAG, "get failed with: " + task.getException()); }
                }
            });
        }
        else { Log.d(TAG, "User could not be loaded!"); }
    }
}