package org.aldomanco.wimhofmethod.ui.prana_breath_section;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PranaBreathViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PranaBreathViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}