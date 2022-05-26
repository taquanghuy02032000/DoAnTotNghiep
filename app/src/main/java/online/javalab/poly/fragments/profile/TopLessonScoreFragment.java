package online.javalab.poly.fragments.profile;

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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import online.javalab.poly.databinding.FragmentTopLessonScoreBinding;
import online.javalab.poly.formatter.MyAxisValueFormatter;
import online.javalab.poly.model.TopLesson;

public class TopLessonScoreFragment extends Fragment {
    private FragmentTopLessonScoreBinding binding;
    private List<BarEntry> barEntries;
    private List<TopLesson> topLessons;
    private static final String BUNDLE_TOP_SCORE = "BUNDLE_TOP_SCORE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTopLessonScoreBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            barEntries = new ArrayList<>();
            topLessons = getArguments().getParcelableArrayList(BUNDLE_TOP_SCORE);
            setData(binding.charTopLesson);
            setupChart(binding.charTopLesson);
        }

        return binding.getRoot();
    }

    public static TopLessonScoreFragment newInstance(List<TopLesson> topLessons) {
        TopLessonScoreFragment fragment = new TopLessonScoreFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(BUNDLE_TOP_SCORE, (ArrayList<? extends Parcelable>) topLessons);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setupChart(BarChart chart) {
        chart.getXAxis().setDrawLabels(false);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setScaleEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.getAxisRight().setEnabled(false);
        chart.getLegend().setEnabled(false);

        //Formatter DateTime
        IAxisValueFormatter xAxisFormatter = new MyAxisValueFormatter(topLessons);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setGranularityEnabled(true);

        MyMarkerView mv = new MyMarkerView(requireContext(), xAxisFormatter);
        mv.setChartView(chart);
        chart.setMarker(mv);
    }

    private void setData(BarChart chart) {
        for (int i = 0; i < topLessons.size(); i++) {
            barEntries.add(new BarEntry(i, topLessons.get(i).getScore()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "01/2022");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(10f);

        BarData barData = new BarData(barDataSet);
        chart.setFitBars(true);

        // add data
        chart.setData(barData);
    }

}
