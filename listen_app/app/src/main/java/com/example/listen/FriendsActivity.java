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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

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
        LayoutInflater inflater = FriendsActivity.this.getLayoutInflater();
        addFriendDialog.setView(inflater.inflate(R.layout.add_friend, null));

        // Dialog button behaviors
        // Add Button
       addFriendDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {


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

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });
    }
}
