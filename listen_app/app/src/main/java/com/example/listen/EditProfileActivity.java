package com.example.listen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    // For the logs (path to this activity)
    private static final String TAG = "EditProfileActivity";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Get the current user
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // ### Custom Methods

    public void showProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

    // ### Overrides

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Locate the form view elements
        final EditText alias = findViewById(R.id.editProfileAlias);
        final EditText bio = findViewById(R.id.editProfileBio);
        final Button submit = findViewById(R.id.editProfileSubmitButton);
        final Button cancel = findViewById(R.id.editProfileCancelButton);

        if (user != null){
            final String uid = user.getUid();
            DocumentReference profRef = db.collection("profiles").document(uid);
            profRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot profile = task.getResult();
                        if (profile.exists()){
                            // Allow the user to edit their profile if it exists (it should)
                            // Set the alias hint to the current alias value in document
                            alias.setHint(profile.getString("alias"));
                            // Set the bio hint to the current bio value in the document
                            bio.setHint(profile.getString("bio"));

                            // Listen for the submit button -> submit changes and back to profile
                            submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //TODO Update the users profile document
                                    
                                    //Load back up the profile activity/view
                                    showProfile();
                                }
                            });
                            // Listen for the cancel button -> submit nothing and back to profile
                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Load back up the profile activity/view
                                    showProfile();
                                }
                            });

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