package com.birdicomputers.login.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.birdicomputers.login.AddTask;
import com.birdicomputers.login.CustomAdapter;
import com.birdicomputers.login.ItemMoveCallBack_Notes;
import com.birdicomputers.login.R;
import com.birdicomputers.login.StartDragListener;
import com.birdicomputers.login.TaskDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class NotesFragment extends Fragment implements StartDragListener{

    StaggeredGridLayoutManager staggaggeredGridLayoutManager;
    private List<String[]> taskList;
    public TaskDBHelper taskDBHelper;
    private ItemTouchHelper itemTouchHelper;
    private static final int ADD_TASK = 1;
    private CustomAdapter adapter;
    private RecyclerView recyclerViewItems;
    private static final String TAG = "TAG";
    private int flag = 0;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel2 = ViewModelProviders.of(this).get(NotesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        flag = 1;
        try {
            taskDBHelper = new TaskDBHelper(this.getContext());
            taskList = taskDBHelper.browseNotes();
            adapter = new CustomAdapter(taskList, (v, position) -> {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            });
            staggaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            recyclerViewItems = root.findViewById(R.id.rv2);
            recyclerViewItems.setLayoutManager(staggaggeredGridLayoutManager);
            recyclerViewItems.setAdapter(adapter);
            ItemTouchHelper.Callback callback = new ItemMoveCallBack_Notes(adapter);
            itemTouchHelper = new ItemTouchHelper(callback);
            itemTouchHelper.attachToRecyclerView(recyclerViewItems);
        } catch (NullPointerException ex) {
            Log.d(TAG, "Exception in setAdapter");
        }
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        taskList = taskDBHelper.browseNotes();
        adapter.setCustomListView(taskList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}