package edu.uw.tcss450.chatapp_group1.ui.weather;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;


public class WeatherListViewModel extends androidx.lifecycle.ViewModel{

    private MutableLiveData<List<WeatherData>> mWeatherList;

    public WeatherListViewModel(@NonNull Application application) {
        //super(application);
        mWeatherList = new MutableLiveData<>();
        mWeatherList.setValue(new ArrayList<>());
    }
    public void addWeatherListObserver(@NonNull LifecycleOwner owner,

                                       @NonNull Observer<? super List<WeatherData>> observer) {
        mWeatherList.observe(owner, observer);
    }

}