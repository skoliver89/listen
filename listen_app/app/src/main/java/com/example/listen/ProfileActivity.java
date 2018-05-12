package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends MenuActivity {
    // ### Class Variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "ProfileActivity";

    // ### Custom Methods
    public void showEditProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    public  void showFriendsList(){
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FloatingActionButton edit = findViewById(R.id.editProfileActionButton);
        FloatingActionButton friends = findViewById(R.id.friendsButton);
        final TextView aliasText = findViewById(R.id.friend_prof_alias);
        final TextView bioText = findViewById(R.id.profileBio);

        if(user != null){
            final String uid = user.getUid();
            DocumentReference profRef = db.collection("profiles").document(uid);
            profRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot profile = task.getResult();
                        if (profile.exists()){
                            aliasText.setText(profile.getString("alias"));
                            bioText.setText(profile.getString("bio"));
                        }
                        else {
                            Log.d(TAG, "Profile Does not exist for: "+ uid);
                        }
                    }
                    else {
                        Log.d(TAG, "Error getting the profile document: " +
                                task.getException());
                    }
                }
            });

        }
        else { Log.d(TAG, "User failed to load"); }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfile();
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriendsList();
            }
        });
    }
}