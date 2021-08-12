package com.birdicomputers.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.security.spec.ECField;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CustomAdapterReminders extends RecyclerView.Adapter<CustomAdapterReminders.MyRecyclerView2> implements ItemMoveCallback.ItemTouchHelperContractReminder{

    public CustomClickListener customClickListener;
    private List<String[]> customListView;
    private String[] mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private View snackbarView;
    private static final String dueDatedTime = "Due on: ";
    private Context context;
    float dpCalculation;
    String[] deleteIndex;
    TaskDBHelper taskDBHelper;

    public void setCustomListView(List<String[]> customListView) {
        this.customListView = customListView;
    }

    public CustomAdapterReminders(List<String[]> customListView, CustomClickListener customClickListener) {
        this.customListView = customListView;
        this.customClickListener = customClickListener;
    }

    @NonNull
    @Override
    public MyRecyclerView2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_item_reminders,parent,false);
        MyRecyclerView2 viewHolder = new MyRecyclerView2(itemView);
        snackbarView = parent.getRootView();
        itemView.setOnClickListener(v -> customClickListener.onItemClick(v, viewHolder.getPosition()));
        dpCalculation = context.getResources().getDisplayMetrics().density;
        return  viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    //Gets data from database and puts them in the Recycler View Layout
    public void onBindViewHolder(@NonNull MyRecyclerView2 holder, int position) {
        String[] temp = customListView.get(position);
        holder.taskText.setText(temp[1]);
        holder.taskDate.setText(dueDatedTime + temp[2] + " at " + temp[3]);
        if(temp[4] != null){
            try {
                holder.customAdapterImage.getLayoutParams().height = (int) (100 * dpCalculation);
                Uri selectedPhoto = Uri.parse(temp[4]); //Uri used for photos inside the device
                //Used for permissions
                context.getContentResolver().takePersistableUriPermission(selectedPhoto, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Picasso.get().setLoggingEnabled(true);  //Third party library to enable image load
                Picasso.get().load(temp[4]).noPlaceholder().centerCrop().fit().into((ImageView) holder.customAdapterImage);
            } catch (Exception e) {
                Log.d("BindView", e.getMessage());
            }
        }
        if(temp[4] == null){
            holder.customAdapterImage.getLayoutParams().height = 0;
        }
    }

    @Override
    public int getItemCount() {
        return customListView.size();
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
            Log.d("TAG", "Error in moving items");
        }
    }

    @Override
    public void onRowSelected(CustomAdapterReminders.MyRecyclerView2 myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.seleted_border);
    }

    @Override
    public void onRowClear(CustomAdapterReminders.MyRecyclerView2 myViewHolder) {
        myViewHolder.rowView.setBackgroundResource(R.drawable.border);
    }

    @Override
    //Swipe to delete
    public void onItemDismiss(int position) {
        deleteIndex = customListView.get(position);
        mRecentlyDeletedItemPosition = position;
        mRecentlyDeletedItem = customListView.get(position);
        taskDBHelper = new TaskDBHelper(context);
        customListView.remove(position);
        showUndoSnackbar();
        taskDBHelper.DeleteFromReminders(deleteIndex[0]);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,getItemCount());
    }

    //Revert delete
    private void undoDelete() {
        try {
            customListView.add(mRecentlyDeletedItemPosition,
                    mRecentlyDeletedItem);
            notifyItemInserted(mRecentlyDeletedItemPosition);
            notifyItemRangeChanged(mRecentlyDeletedItemPosition, getItemCount());
            taskDBHelper = new TaskDBHelper(context);
            taskDBHelper.addTask(deleteIndex[1], deleteIndex[2],
                    deleteIndex[3], deleteIndex[4], deleteIndex[5]);
        }
        catch (Exception e){
            Log.d("UndoDelete", e.getMessage());
        }
    }

    private void showUndoSnackbar() {
        Snackbar.make(snackbarView, "Task deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoDelete()).show();
    }

    public class MyRecyclerView2 extends RecyclerView.ViewHolder {
        TextView taskText, taskDate;
        ImageView customAdapterImage;
        View rowView;
        private MyRecyclerView2(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            taskText = itemView.findViewById(R.id.reminderstextView);
            taskDate = itemView.findViewById(R.id.remindersDateView);
            customAdapterImage = itemView.findViewById(R.id.customAdapterImage);
        }
    }
}