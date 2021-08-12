package com.birdicomputers.login;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class ItemMoveCallback extends ItemTouchHelper.Callback {

    private ItemTouchHelperContractReminder mAdapterReminder;
    public ItemMoveCallback( ItemTouchHelperContractReminder mAdapterReminder) {
        this.mAdapterReminder = mAdapterReminder;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        mAdapterReminder.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapterReminder.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof CustomAdapterReminders.MyRecyclerView2) {
                CustomAdapterReminders.MyRecyclerView2 myViewHolder= (CustomAdapterReminders.MyRecyclerView2) viewHolder;
                mAdapterReminder.onRowClear(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof CustomAdapterReminders.MyRecyclerView2) {
            CustomAdapterReminders.MyRecyclerView2 myViewHolder= (CustomAdapterReminders.MyRecyclerView2) viewHolder;
            mAdapterReminder.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContractReminder {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(CustomAdapterReminders.MyRecyclerView2 myViewHolder);
        void onRowClear(CustomAdapterReminders.MyRecyclerView2 myViewHolder);
        void onItemDismiss(int position);
    }
}
