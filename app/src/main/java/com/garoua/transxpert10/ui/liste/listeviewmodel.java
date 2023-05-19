package com.garoua.transxpert10.ui.liste;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class listeviewmodel extends ViewModel {

    private final MutableLiveData<String> mText;

    public listeviewmodel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}