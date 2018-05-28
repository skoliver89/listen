package com.example.listen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FriendProfileActivity extends AppCompatActivity {
    // ## Class Variables
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = FriendProfileActivity.class.getSimpleName();

    // ## Custom Methods
    private String getFriendUID(){
        String friendUID = null;
        if(getIntent().hasExtra("uid")){
            friendUID = getIntent().getStringExtra("uid");
        }

        return friendUID;
    }
    private void setProfileData(){
        DocumentReference profRef = db.collection("profiles").document(getFriendUID());
        profRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot profile = task.getResult();
                    if (profile.exists()){
                        TextView aliasText = findViewById(R.id.friend_prof_alias);
                        TextView bioText = findViewById(R.id.friend_profile_bio);
                        String alias = profile.getString("alias");
                        String bio = profile.getString("bio");

                        aliasText.setText(alias);
                        bioText.setText(bio);
                    }
                    else { Log.d(TAG, "No such profile."); }
                }
                else { Log.d(TAG, "Failed to get Friend's Profile: " + task.getException()); }
            }
        });
    }
    // ## Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        setProfileData();
    }
}
