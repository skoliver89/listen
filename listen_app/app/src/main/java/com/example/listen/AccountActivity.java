package com.example.listen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    // ## Class Variables
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AccountActivity";
    TextView mTextView;
    FloatingActionButton delAccount;

    // ## Custom Methods
    public void deleteAccount(){
        // Initiate the alert pop-up
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // Add the alert buttons
        alert.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hide the Dialog
                dialog.dismiss();
                // Clicked Delete button - Delete the account
                String uid = user.getUid();
                //Get a ref to the user's profile document
                DocumentReference profRef = db.collection("profiles").document(uid);
                // Delete the profile data
                profRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            deleteUser();
                        }
                        else{
                            Log.d(TAG, "Failed to delete the user profile.");
                        }
                    }
                });
            }
        });
        alert.setNegativeButton(R.string.cancelButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Clicked Cancel button - Do nothing.
            }
        });
        // Set the alert title and message
        alert.setTitle("Delete Your Account?");
        alert.setMessage("Please confirm that you want to delete your listen account.");
        // Create the alert
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void deleteUser(){
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    AuthUI.getInstance()
                            .signOut(getApplicationContext())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // After sign out restart the app
                                    Intent i = getBaseContext()
                                            .getPackageManager()
                                            .getLaunchIntentForPackage(
                                                    getBaseContext()
                                                            .getPackageName() );
                                    if (i != null) {
                                        i.addFlags(Intent
                                                .FLAG_ACTIVITY_CLEAR_TOP);
                                    }
                                    startActivity(i);
                                }
                            });
                }
                else {
                    Log.d(TAG, "Failed to delete the user from auth");
                }
            }
        });
    }

    // ## Overrides
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String name = user.getDisplayName();
        String email = user.getEmail();

        //Set the username
        mTextView = findViewById(R.id.text_username);
        mTextView.setText(name);

        //Set the email
        mTextView = findViewById(R.id.text_email);
        mTextView.setText(email);

        //Delete Account button
        delAccount = findViewById(R.id.deleteAccountbutton);
        delAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }
}
