package com.garoua.transxpert10.ui.ajout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AjoutViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AjoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Ajout fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}