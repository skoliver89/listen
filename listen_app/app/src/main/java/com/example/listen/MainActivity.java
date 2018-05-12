package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ### Class Variables
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "MainActivity";

    private static final int RC_SIGN_IN = 123;

    // ### Custom Methods

    // Choose authentication providers
    @SuppressWarnings("deprecation")
    List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.Builder(
            AuthUI.GOOGLE_PROVIDER).build());

    public void authenticate(){
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void profileExists(final FirebaseUser user){
        String uid = user.getUid();
        DocumentReference profRef = db.collection("profiles").document(uid);
        profRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot profile = task.getResult();
                    if (profile.exists()){
                        //The user has made a profile before, load profile view activity
                        showProfile();
                    }
                    else{
                        //The user has not made a profile before, load new profile activity
                        createProfile(user);
                    }
                }
                else{
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // ### Activity Navigation

    //Load up the NewUserProfile activity
    public void createProfile(FirebaseUser user)
    {
        Intent intent = new Intent(this, NewUserProfile.class);

        intent.putExtra("uid", user.getUid());
        startActivity(intent);
    }
    public void showProfile(){
        Intent intent = new Intent(this, ProfileActivity.class);

        startActivity(intent);
    }

    // ### Overriding Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                profileExists(user);
            } else {
                // Sign in failed, reload the app to the login prompt
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                if (i != null) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(i);
            }
        }
    }
}
