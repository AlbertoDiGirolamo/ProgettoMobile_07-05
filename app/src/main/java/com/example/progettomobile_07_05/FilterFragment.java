package com.example.progettomobile_07_05;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;


public class FilterFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fl = getActivity().findViewById(R.id.fab_add);
        fl.setVisibility(View.INVISIBLE);
        SearchView searchView = getActivity().findViewById(R.id.search_icon);
        searchView.setVisibility(View.INVISIBLE);

        NumberPicker numberPickerMin = getActivity().findViewById(R.id.priceminpicker);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(101);
        NumberPicker numberPickerMax = getActivity().findViewById(R.id.pricemaxpicker);
        numberPickerMax.setMinValue(0);
        numberPickerMax.setMaxValue(101);
        String[] values = new String[102];
        values[0] = "●";
        for (int i =0;i<=100;i++){
            values[i+1] = String.valueOf(i)+" €";
        }
        numberPickerMin.setDisplayedValues(values);
        numberPickerMax.setDisplayedValues(values);
        TextView rangefilter = getActivity().findViewById(R.id.rangefilter);

        if (((MainActivity)getActivity()).getCircle()==null){
            rangefilter.setText("5 Km");
        }else{
            rangefilter.setText(((int) ((MainActivity)getActivity()).getCircle().getRadius()/ 1000)+" Km");
        }



        getActivity().findViewById(R.id.openmapbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.insertFragment((AppCompatActivity) getActivity(), new MapFragment(), MapFragment.class.getSimpleName());
            }
        });
        getActivity().findViewById(R.id.savefilter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (numberPickerMax.getValue() < numberPickerMin.getValue()){
                    if (numberPickerMax.getValue() != 0){
                        Toast.makeText(getActivity(), "Input prezzo non corretto", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                ((MainActivity)getActivity()).setMinPrice( numberPickerMin.getValue()-1);
                ((MainActivity)getActivity()).setMaxPrice( numberPickerMax.getValue()-1);
                //Log.d("prova", String.valueOf(numberPickerMax.getValue()-1));
                Utilities.insertFragment((AppCompatActivity) getActivity(), new HomeFragment(), HomeFragment.class.getSimpleName());
            }
        });

        numberPickerMax.setValue(((MainActivity)getActivity()).getMaxPrice()+1);
        numberPickerMin.setValue(((MainActivity)getActivity()).getMinPrice()+1);

    }
}
