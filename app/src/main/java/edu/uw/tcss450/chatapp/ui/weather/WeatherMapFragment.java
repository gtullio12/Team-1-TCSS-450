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
import edu.uw.tcss450.chatapp.databinding.FragmentWeatherMapBinding;

/**
 * A simple {@link Fragment} subclass.

 */
public class WeatherMapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentWeatherMapBinding binding = FragmentWeatherMapBinding.bind(getView());
        //Note argument sent to the ViewModelProvider constructor. It is the Activity that
        //holds this fragment.
        //UserInfoViewModel model = new ViewModelProvider(getActivity())
        //        .get(UserInfoViewModel.class);
        //On button click, navigate to Third Home

//        binding.buttonConfirmLocation.setOnClickListener(button ->
//                Navigation.findNavController(getView()).navigate(
//                        WeatherMapFragmentDirections.actionWeatherMapFragmentToWeatherFragment())
//        );



    }


}