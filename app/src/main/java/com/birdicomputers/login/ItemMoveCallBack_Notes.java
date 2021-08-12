package com.birdicomputers.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ItemMoveCallBack_Notes extends ItemTouchHelper.Callback {
    private ItemTouchHelperContract itemTouchHelperContract;

    public ItemMoveCallBack_Notes(ItemTouchHelperContract itemTouchHelperContract) {
        this.itemTouchHelperContract = itemTouchHelperContract;
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
        itemTouchHelperContract.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        itemTouchHelperContract.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof CustomAdapter.MyRecyclerView) {
                    CustomAdapter.MyRecyclerView myViewHolder = (CustomAdapter.MyRecyclerView) viewHolder;
                    itemTouchHelperContract.onRowClear(myViewHolder);
                }
            }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof CustomAdapter.MyRecyclerView) {
            CustomAdapter.MyRecyclerView myViewHolder= (CustomAdapter.MyRecyclerView) viewHolder;
            itemTouchHelperContract.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract {
        void onRowMoved(int fromPosition, int toPosition);
        void onRowSelected(CustomAdapter.MyRecyclerView myViewHolder);
        void onRowClear(CustomAdapter.MyRecyclerView myViewHolder);
        void onItemDismiss(int position);
    }
}
