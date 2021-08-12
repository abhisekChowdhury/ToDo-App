package com.birdicomputers.login.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.birdicomputers.login.ui.reminders.HomeFragment;

public class NotesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

    }
    public LiveData<String> getText() {
        return mText;
    }
}