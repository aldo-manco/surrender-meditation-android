package org.aldomanco.wimhofmethod.ui.yoga_section;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YogaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YogaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}