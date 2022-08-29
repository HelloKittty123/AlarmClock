package com.samsung.alarmteam6;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;


public class StopwatchFragment extends Fragment implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, NumberPicker.Formatter{
    NumberPicker hourPicker, minutePicker, secondPicker;
    int hour,minute, second;
    ImageButton btnStart;


    public StopwatchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hourPicker = getActivity().findViewById(R.id.hour_picker);
        minutePicker = getActivity().findViewById(R.id.minute_picker);
        secondPicker = getActivity().findViewById(R.id.second_picker);
        Intent intent = new Intent(getActivity().getApplicationContext(), TimePicker.class);
        init();
        btnStart = getActivity().findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("hour",hour);
                intent.putExtra("minute", minute);
                intent.putExtra("second", second);
                startActivity(intent);
            }
        });
    }


    public String format(int value){
        String tmp = String.valueOf(value);
        if(value<10){
            tmp = "0"+tmp;
        }
        return tmp;
    }
    public void init(){
        hourPicker.setFormatter(this::format);
        hourPicker.setOnValueChangedListener(this::onValueChange);
        hourPicker.setOnScrollListener(this::onScrollStateChange);
        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);
        hourPicker.setValue(0);

        minutePicker.setFormatter(this::format);
        minutePicker.setOnValueChangedListener(this::onValueChange);
        minutePicker.setOnScrollListener(this::onScrollStateChange);
        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        minutePicker.setValue(0);
        secondPicker.setFormatter(this::format);
        secondPicker.setOnValueChangedListener(this::onValueChange);
        secondPicker.setOnScrollListener(this::onScrollStateChange);
        secondPicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setValue(0);
        hourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                hour = i1;
            }
        });
        minutePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                minute = i1;
            }
        });
        secondPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                second = i1;
            }
        });

    }

    @Override
    public void onScrollStateChange(NumberPicker numberPicker, int i) {
    }

    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
    }
}