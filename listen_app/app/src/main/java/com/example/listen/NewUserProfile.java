package com.example.listen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewUserProfile extends AppCompatActivity {

    // ### Class Variables
    private static final String TAG = "NewUserProfile";
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ### Custom Methods

    // Sign-out the user
    // Used if the user does not have and neglects to initialize a profile
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // After sign out restart the app -> login prompt
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        if (i != null) {
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        startActivity(i);
                    }
                });
    }

    public void showProfile(){
        //start the activity for the account view (TODO: Change to ProfileActivity when made)
        startActivity(new Intent(this, MainActivity.class));
    }

    // ### Overriding Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_profile);

        // Submit Button Behavior - Submit data to profiles->#profileDoc(uid)# in DB
        // Find the Submit Button
        final Button submitButton = findViewById(R.id.newProfileSubmitButton);
        // Create a listener for the Submit Button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the uid value from the Intent's StringExtra
                // Used as the profile document name
                String uid = getIntent().getStringExtra("uid");
                // Get the values for the profile document
                // alias value
                EditText aliasET = findViewById(R.id.newProfileAlias);
                String alias = aliasET.getText().toString();
                // bio value
                EditText bioET = findViewById(R.id.newProfileBio);
                String bio = bioET.getText().toString();
                //store the profile values in a Map
                Map<String, Object> profData = new HashMap<>();
                profData.put("alias", alias);
                profData.put("bio", bio);
                //Create the Profile Document for the user in FireStore
                //On Success Load the profile view
                db.collection("profiles").document(uid)
                        .set(profData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Load the Profile View Activity
                                //For now load in the main activity until the view is made
                                showProfile();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //If it fails log it
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });

        // Cancel Button Behavior - Send to log-in prompt
        // Find the Cancel button
        final Button cancelButton = findViewById(R.id.newProfileCancelButton);
        // Create a listener for the Cancel Button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign the user out if they cancel profile creation
                signOut();
            }
        });

    }
}
