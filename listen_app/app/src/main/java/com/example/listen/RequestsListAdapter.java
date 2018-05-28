package com.example.listen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {
    // ### Class Variables
    public List<Friend> requestList;
    private Context mContext;

    // ### Constructors
    public RequestsListAdapter(Context mContext, List<Friend> requestList) {
        this.requestList = requestList;
        this.mContext = mContext;
    }

    // ### Custom Methods
    private void requestAction(String uid, String alias){
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

            }
        });

        // Deny Button
        requestDialog.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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
                //TODO Dialog to accept or deny friend requests
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
