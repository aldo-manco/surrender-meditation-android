package org.aldomanco.wimhofmethod.ui.home;

import android.content.Intent;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.aldomanco.wimhofmethod.R;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> textRunningRounds;

    public HomeViewModel() {
        textRunningRounds = new MutableLiveData<>();
        textRunningRounds.setValue("No Round Done");
    }

    public LiveData<String> getText() {
        return textRunningRounds;
    }

    public void setTextView(int numberOfRounds, View view){

        switch (view.getId()){

            case R.id.layout:
            case R.id.text_home:

                textRunningRounds.setValue("Round " + numberOfRounds + " in progress");

                break;
        }
    }

    public void stopTextView(int numberOfRounds, View view){

        switch (view.getId()){

            case R.id.layout:
            case R.id.text_home:

                textRunningRounds.setValue("Round " + numberOfRounds + " has been stopped");

                break;
        }
    }

}