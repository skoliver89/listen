package com.example.listen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {
    // ### Class Variables
    public List<Friend> requestList;
    private Context mContext;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String TAG = RequestsListAdapter.class.getSimpleName();

    // ### Constructors
    public RequestsListAdapter(Context mContext, List<Friend> requestList) {
        this.requestList = requestList;
        this.mContext = mContext;
    }

    // ### Custom Methods
    private void requestAction(final String uid, final String alias){
        // Create the Dialog for accepting or denying a request
        AlertDialog.Builder requestDialog = new AlertDialog
                .Builder(mContext);

        // Set the Dialog Title
        requestDialog.setTitle("Friend Request");

        // Set the Dialog Message
        String msg = alias + " would like to be your friend.";
        requestDialog.setMessage(msg);

        // Dialog Button Behaviors
        // Accept Button
        requestDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO Accept button logic
                // Reference each user's profile
                final DocumentReference meRef = db.collection("profiles")
                        .document(user.getUid());
                final DocumentReference themRef = db.collection("profiles")
                        .document(uid);

                // Map the friends data
                // Add each user to each other's friend lists

                // My Friend List
                meRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            // My profile data from Firestore (snapshot)
                            DocumentSnapshot myProf = task.getResult();
                            // Set My Data
                            Map<String, Object> me = new HashMap<>();
                            me.put("alias", myProf.getString("alias"));
                            // Add me to their friend list
                            themRef.collection("friends").document(user.getUid()).set(me);
                        }
                        else { Log.e(TAG, "Error getting my profile snapshot."); }
                    }
                });

                // Their Friend List
                themRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            // Their profile data from Firestore (snapshot)
                            DocumentSnapshot theirProf = task.getResult();
                            Map<String, Object> them = new HashMap<>();
                            them.put("alias", theirProf.getString("alias"));
                            // Add them to my friend list
                            meRef.collection("friends").document(uid).set(them);
                        }
                        else { Log.e(TAG, "Error getting their profile."); }
                    }
                });

                // Delete the request from my list of pending requests
                meRef.collection("requests").document(uid).delete();

                // Check for similar request in other user's list of pending requests.
                // I.E. Check if I sent them a request as well.
                // If one exists - Delete it too
                themRef.collection("requests").document(user.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot myRequest = task.getResult();
                            if (myRequest.exists()){
                                myRequest.getReference().delete();
                            }
                        }
                        else { Log.e(TAG, "Error getting their requests."); }
                    }
                });

            }
        });

        // Deny Button
        requestDialog.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the request
                db.collection("profiles").document(user.getUid()).collection("requests")
                        .document(uid).delete();
            }
        });

        // Show the dialog
        requestDialog.show();
    }

    // ### Overrides
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.aliasText.setText(requestList.get(position).getAlias());

        //Make the items clickable
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = requestList.get(position).getUid();
                String alias = requestList.get(position).getAlias();
                requestAction(uid, alias);
            }
        });
    }

    @Override
    public int getItemCount(){ return requestList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView aliasText;

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            aliasText = mView.findViewById(R.id.rl_alias_text);
        }
    }
}
