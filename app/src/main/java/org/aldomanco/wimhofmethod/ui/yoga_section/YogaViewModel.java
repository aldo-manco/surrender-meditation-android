package org.aldomanco.wimhofmethod.ui.yoga_section;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.aldomanco.wimhofmethod.R;

public class YogaViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YogaViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public void setTextView(View view){

        switch (view.getId()){

            case R.id.playShortSession:
            case R.id.playLongSession:

                mText.setValue("");

                break;
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}