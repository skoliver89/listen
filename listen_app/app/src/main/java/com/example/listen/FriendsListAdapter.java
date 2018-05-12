package com.example.listen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.ViewHolder> {

    public List<Friends> friendsList;
    public FriendsListAdapter(List<Friends> friendsList){
        this.friendsList = friendsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.aliasText.setText(friendsList.get(position).getAlias());
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
