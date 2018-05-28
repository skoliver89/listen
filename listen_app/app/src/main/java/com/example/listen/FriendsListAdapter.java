package com.example.listen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    public List<Friend> friendsList;
    private Context mContext;

    public FriendsListAdapter(Context context, List<Friend> friendsList){
        this.friendsList = friendsList;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.aliasText.setText(friendsList.get(position).getAlias());

        //Make the items clickable
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FriendProfileActivity.class);
                intent.putExtra("uid", friendsList.get(position).getUid());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public TextView aliasText;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            aliasText = mView.findViewById(R.id.fl_alias_text);
        }
    }
}
