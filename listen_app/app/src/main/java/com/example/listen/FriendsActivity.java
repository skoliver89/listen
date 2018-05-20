package com.example.listen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//References: https://developer.android.com/guide/topics/ui/layout/recyclerview#java
//            https://www.youtube.com/watch?v=kyGVgrLG3KU&t=2s
public class FriendsActivity extends AppCompatActivity {

    // ### Class Variables
    private static final String TAG = FriendsActivity.class.getSimpleName();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private RecyclerView.Adapter mAdapter;

    private List<Friends> friendsList;

    // ### Custom Methods

    // Create and show a dialog to find a friend to add
    private void addFriend(){
        // Create the Dialog for adding a friend
        final AlertDialog.Builder addFriendDialog = new AlertDialog
                .Builder(FriendsActivity.this);

        // Set the Dialog Title
        addFriendDialog.setTitle("Add a Friend");

        // Set the Dialog Message
        addFriendDialog.setMessage("Request a friend by E-mail:");

        // Add the EditText to the Dialog
        final EditText input = new EditText(FriendsActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS |
                InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
          LinearLayout.LayoutParams.MATCH_PARENT
        );

        input.setLayoutParams(layoutParams);
        addFriendDialog.setView(input);

        // Dialog button behaviors
        // Add Button
       addFriendDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               final String email = input.getText().toString();
               if (!email.isEmpty() && isEmail(email)){
                   db.collection("profiles").whereEqualTo("email", email).limit(1).get()
                           .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                               @Override
                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                   if (task.isSuccessful()){
                                       if (!task.getResult().isEmpty()){
                                           DocumentSnapshot prof = task.getResult()
                                                   .getDocuments().get(0);
                                           String uid = prof.getId();
                                           if (!isSelf(uid)){
                                               checkAlreadyFriends(uid);
                                           }
                                           else {
                                               Toast.makeText(FriendsActivity.this,
                                                       "Error: Cannot add yourself.",
                                                       Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                       else {
                                           Toast.makeText(FriendsActivity.this,
                                                   "No user found: " + email,
                                                   Toast.LENGTH_SHORT).show();
                                       }
                                   }
                                   else { Log.d(TAG, "error querying for: " + email); }
                               }
                           });
               }
               else{
                   Toast.makeText(FriendsActivity.this, "Invalid Email.",
                           Toast.LENGTH_SHORT).show();
               }
               // Try to find to requested user's info

           }
       });

        // Cancel Button
        addFriendDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Nothing, Just close the dialog;
            }
        });

        // Show the dialog
        addFriendDialog.show();
    }
    private void checkAlreadyFriends(final String uid){
        final String myUID = user.getUid();
        db.collection("profiles").document(myUID).collection("friends").document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        Toast.makeText(FriendsActivity.this,
                                "You are already friends.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        sendRequest(uid);
                    }
                }
                else { Log.d(TAG,"Failed to get friends for: " + myUID); }
            }
        });
    }

    // Create (Send) a Friend Request
    private void sendRequest(String uid){
        final String theirUID = uid;
        final String myUID = user.getUid();
        db.collection("profiles").document(myUID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult().exists()){
                                DocumentSnapshot profile = task.getResult();
                                final String myAlias = profile.getString("alias");

                                Map<String, Object> request = new HashMap<>();
                                request.put("alias", myAlias);
                                request.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("profiles").document(theirUID)
                                        .collection("requests")
                                        .document(myUID).set(request)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(FriendsActivity.this,
                                                "Friend Request Sent.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                Toast.makeText(FriendsActivity.this,
                                        "Couldn't find myself", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else { Log.d(TAG, "Failed to get document..."); }
                    }
                });

        //Toast.makeText(FriendsActivity.this, myAlias, Toast.LENGTH_SHORT).show();

       /* */
    }

    // Check if a given uid is the same as the uid of the current user
    private boolean isSelf(String uid){
        return uid.equals(user.getUid());
    }


    // Check if the entered text is an email address
    public boolean isEmail (String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // ### Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        FloatingActionButton addFriend = findViewById(R.id.addFriendButton);

        friendsList = new ArrayList<>();

        RecyclerView mRecyclerView = findViewById(R.id.friends_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FriendsListAdapter(this, friendsList);
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
                        String uid = doc.getDocument().getId();
                        friends.setFriendUID(uid);
                        friendsList.add(friends);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }
}
