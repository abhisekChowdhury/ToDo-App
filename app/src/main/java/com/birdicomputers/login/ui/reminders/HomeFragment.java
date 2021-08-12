package com.birdicomputers.login.ui.reminders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.birdicomputers.login.AddTask;
import com.birdicomputers.login.CustomAdapterReminders;
import com.birdicomputers.login.ItemMoveCallback;
import com.birdicomputers.login.R;
import com.birdicomputers.login.StartDragListener;
import com.birdicomputers.login.TaskDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onesignal.OneSignal;
import java.util.List;

public class HomeFragment extends Fragment implements StartDragListener {
    private List<String[]> taskList;
    private ItemTouchHelper touchHelper;
    private TaskDBHelper taskDBHelper;
    private HomeViewModel homeViewModel;
    CustomAdapterReminders adapter;
    private StaggeredGridLayoutManager staggaggeredGridLayoutManager;
    private static final int ADD_TASK = 1;
    private static final String TAG = "TAG";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        OneSignal.startInit(this.getContext())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        TaskDBHelper taskDBHelper = new TaskDBHelper(this.getContext());
        taskList = taskDBHelper.browseReminders();
        adapter = new CustomAdapterReminders(taskList, (v, position) -> {
            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            Intent addTask = new Intent(getActivity(), AddTask.class);
            String temp[] = taskList.get(position);
            Log.d("Id", temp[0]);
            addTask.putExtra("clickedPosition", temp[0]);
            startActivityForResult(addTask,1);
        });
        staggaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        RecyclerView recyclerViewItems = root.findViewById(R.id.rv);
        recyclerViewItems.setLayoutManager(staggaggeredGridLayoutManager);
        recyclerViewItems.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewItems);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent addTask = new Intent(getActivity(), AddTask.class);
            addTask.putExtra("Itemcount", String.valueOf(adapter.getItemCount()));
            startActivityForResult(addTask,ADD_TASK);
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TaskDBHelper taskDBHelper = new TaskDBHelper(this.getContext());
        taskList = taskDBHelper.browseReminders();
        adapter.setCustomListView(taskList);    //throwing data into adapter
        adapter.notifyDataSetChanged();         //Refreshing the screen
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder) {
        touchHelper.startDrag(viewHolder);
    }
}