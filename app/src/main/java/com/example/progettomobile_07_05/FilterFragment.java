package com.example.progettomobile_07_05;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        NumberPicker numberPickerMin = getActivity().findViewById(R.id.priceminpicker);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(101);
        NumberPicker numberPickerMax = getActivity().findViewById(R.id.pricemaxpicker);
        numberPickerMax.setMinValue(0);
        numberPickerMax.setMaxValue(101);
        String[] values = new String[102];
        values[0] = "●";
        for (int i =0;i<=100;i++){
            values[i+1] = String.valueOf(i);
        }
        numberPickerMin.setDisplayedValues(values);
        numberPickerMax.setDisplayedValues(values);

        Spinner spinnerCategory = getActivity().findViewById(R.id.category_spinner);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Insalata");
        arrayList.add("Cipolle");
        arrayList.add("Fragole");
        arrayList.add("Banane");
        arrayList.add("Albicocche");
        arrayList.add("Pesche");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        spinnerCategory.setAdapter(arrayAdapter);
        getActivity().findViewById(R.id.editText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinnerCategory.performClick();
            }
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String product = (String) parent.getAdapter().getItem(position);

                EditText editText =getActivity().findViewById(R.id.editText);
                editText.setText(product);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
