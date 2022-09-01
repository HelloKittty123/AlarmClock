package com.samsung.alarmteam6;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samsung.alarmteam6.adapters.AdapterStopWatch;
import com.samsung.alarmteam6.models.Product;

import java.util.ArrayList;


public class TimerFragment extends Fragment {
    FloatingActionButton btnStart, btnStop, btnList, btnPause;
    ImageView icanchor;
    long mLastTime;
    Chronometer timerHere;
    ArrayList<Product> mPro = new ArrayList<Product>();
    ListView listViewProduct;
    AdapterStopWatch proAdapterStopWatch;
    private ObjectAnimator animator;
    int i;


    public TimerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnStart = getActivity().findViewById(R.id.btnStartTimer);
        icanchor = getActivity().findViewById(R.id.icanchorTimer);
        btnStop = getActivity().findViewById(R.id.btnStopTimer);
        btnList = getActivity().findViewById(R.id.btnList);
        btnPause = getActivity().findViewById(R.id.btnPauseTimer);
        timerHere = getActivity().findViewById(R.id.timerHere);
        listViewProduct = getActivity().findViewById(R.id.list_time);
        proAdapterStopWatch = new AdapterStopWatch(getActivity().getApplicationContext(), mPro);
        listViewProduct.setAdapter(proAdapterStopWatch);
        btnPause.setVisibility(View.GONE);
        btnList.setVisibility(View.GONE);
        btnStop.setVisibility(View.GONE);
        i = 0;
        mLastTime = 0;
        animator = ObjectAnimator.ofFloat(icanchor, "rotation", 0, 360);
        animator.setDuration(8000);
        animator.setRepeatCount(0xffffffff);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnStart.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                btnList.setVisibility(View.VISIBLE);
                if (mLastTime == 0) {
                    timerHere.setBase(SystemClock.elapsedRealtime());
                    animator.start();
                } else {
                    long intervalOnPause = (SystemClock.elapsedRealtime() - mLastTime);
                    timerHere.setBase(timerHere.getBase() + intervalOnPause);
                }
                animator.resume();
                timerHere.start();
                btnStop.setVisibility(View.GONE);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                mLastTime = 0;
                animator.end();
                timerHere.setBase(SystemClock.elapsedRealtime());
                timerHere.stop();
                mPro.clear();
                proAdapterStopWatch.notifyDataSetChanged();
                btnStop.setVisibility(View.GONE);

            }
        });
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (i == 0) {
                    long time = SystemClock.elapsedRealtime() - timerHere.getBase();
                    String t = returnTime(time);
                    mPro.add(new Product(i + 1, "+" + t, t));
                } else {
                    long time = SystemClock.elapsedRealtime() - timerHere.getBase();
                    String t = returnTime(time);
                    String t1 = minusTime(mPro.get(i - 1).getTimer(), t);
                    mPro.add(new Product(i + 1, "+" + t1, t));
                }
                i++;
                proAdapterStopWatch.notifyDataSetChanged();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerHere.stop();
                mLastTime = SystemClock.elapsedRealtime();
                animator.pause();
                btnPause.setVisibility(View.GONE);
                btnList.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.VISIBLE);

            }
        });
    }

    public String returnTime(long time) {
        int h = (int) (time / 3600000);
        int m = (int) (time - h * 3600000) / 60000;
        int s = (int) (time - h * 3600000 - m * 60000) / 1000;
        String t = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
        return t;
    }

    public String minusTime(String timeStart, String timeEnd) {
        String path1[] = timeStart.split(":");
        String path2[] = timeEnd.split(":");
        int m = Integer.parseInt(path2[0]) - Integer.parseInt(path1[0]);
        int s = Integer.parseInt(path2[1]) - Integer.parseInt(path1[1]);
        String t = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
        return t;

    }
}