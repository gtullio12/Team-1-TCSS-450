package edu.uw.tcss450.chatapp.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.chatapp.R;
import edu.uw.tcss450.chatapp.databinding.FragmentWeatherDailyListBinding;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class WeatherDailyListFragment extends Fragment {
    private WeatherListViewModel mModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_hourly_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(WeatherListViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWeatherDailyListBinding binding = FragmentWeatherDailyListBinding.bind(getView());


        mModel.addWeatherListObserver(getViewLifecycleOwner(), weatherList -> {
            if (!weatherList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new WeatherDailyRecyclerViewAdapter(weatherList)
                );
            }
        });
    }

}