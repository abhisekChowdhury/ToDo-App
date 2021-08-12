package com.birdicomputers.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyRecyclerView> implements ItemMoveCallBack_Notes.ItemTouchHelperContract {
    public void setCustomListView(List<String[]> customListView) {
        this.customListView = customListView;
    }

    private List<String[]> customListView;
    View snackbarView;
    private String[] mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private Context context;
    private float dpCalculation;
    private CustomClickListener customClickListener;
    public CustomAdapter(List<String[]> customTextView, CustomClickListener customClickListener) {
        this.customListView = customTextView;
        this.customClickListener = customClickListener;
    }

    @NonNull
    @Override
    public MyRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_items,parent,false);
        MyRecyclerView viewHolder = new MyRecyclerView(itemView);
        snackbarView = parent.getRootView();
        itemView.setOnClickListener(v -> customClickListener.onItemClick(itemView, viewHolder.getPosition()));
        dpCalculation = context.getResources().getDisplayMetrics().density;
        return  viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerView holder, int position) {
        String[] temp = customListView.get(position);
        holder.customTextView.setText(temp[1]);
    }

    @Override
    public int getItemCount() {
        return customListView.size();
    }

    @Override
    public void onItemDismiss(int position) {
        String[] deleteIndex = customListView.get(position);
        mRecentlyDeletedItemPosition = position;
        mRecentlyDeletedItem = customListView.get(position);
        customListView.remove(position);
        TaskDBHelper taskDBHelper = new TaskDBHelper(context);
        final Handler handler = new Handler();
        showUndoSnackbar();
        handler.postDelayed(() -> taskDBHelper.DeleteFromNotes(deleteIndex[0]), 6000);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount()-position);
    }

    private void undoDelete() {
        customListView.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
        notifyItemRangeChanged(mRecentlyDeletedItemPosition,getItemCount());
    }

    private void showUndoSnackbar() {
        Snackbar.make(snackbarView, "Task deleted.", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoDelete()).show();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        try {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(customListView, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(customListView, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        } catch (Exception ex) {
        }
    }

    @Override
    public void onRowSelected(CustomAdapter.MyRecyclerView myViewHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            myViewHolder.rowView.setBackgroundResource(R.drawable.border);
        }
    }

    @Override
    public void onRowClear(CustomAdapter.MyRecyclerView myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.seleted_border);
    }

    public class MyRecyclerView extends RecyclerView.ViewHolder {
        TextView customTextView;
        View rowView;
        public MyRecyclerView(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            customTextView = itemView.findViewById(R.id.notestextView);
        }
    }
}
