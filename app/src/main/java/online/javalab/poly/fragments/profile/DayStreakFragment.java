package online.javalab.poly.fragments.profile;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import online.javalab.poly.databinding.FragmentDayStreakBinding;
import online.javalab.poly.formatter.DayAxisValueFormatter;
import online.javalab.poly.model.Profile;

public class DayStreakFragment extends Fragment {
    private FragmentDayStreakBinding binding;
    private List<BarEntry> barEntries;
    private List<Profile> profiles;
    private static final String TAG = "TAG";
    private static final String BUNDLE_DAY_STREAK = "BUNDLE_DAY_STREAK";
    private static final String BUNDLE_PROFILE = "BUNDLE_PROFILE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDayStreakBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            barEntries = new ArrayList<>();
            profiles = new ArrayList<>();
            barEntries = getArguments().getParcelableArrayList(BUNDLE_DAY_STREAK);
            profiles = getArguments().getParcelableArrayList(BUNDLE_PROFILE);
            setData(binding.chartDayStreak);
            setupChart(binding.chartDayStreak);
        }

        return binding.getRoot();
    }

    public static DayStreakFragment newInstance(List<BarEntry> barEntries, List<Profile> profiles) {
        DayStreakFragment fragment = new DayStreakFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_DAY_STREAK, (ArrayList<? extends Parcelable>) barEntries);
        args.putParcelableArrayList(BUNDLE_PROFILE, (ArrayList<? extends Parcelable>) profiles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupChart(BarChart chart) {
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(false);
        chart.setMaxVisibleValueCount(60);
        chart.setPinchZoom(false);
        chart.getAxisRight().setEnabled(false);

        //Formatter DateTime
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(binding.chartDayStreak, profiles);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }

    private void setData(BarChart chart) {
        // Show current month & year
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
        Date date = new Date();

        BarDataSet barDataSet = new BarDataSet(barEntries, dateFormat.format(date));
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(14f);

        BarData barData = new BarData(barDataSet);
        chart.setFitBars(true);

        // add data
        chart.setData(barData);
    }

}
