package com.example.listen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {
    public List<Friend> requestList;
    private Context mContext;

    public RequestsListAdapter(List<Friend> requestList, Context mContext) {
        this.requestList = requestList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        holder.aliasText.setText(requestList.get(position).getAlias());

        //Make the items clickable
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Dialog to accept or deny friend requests
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
