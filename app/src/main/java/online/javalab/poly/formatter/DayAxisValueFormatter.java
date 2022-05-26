package online.javalab.poly.formatter;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import online.javalab.poly.model.Profile;

public class DayAxisValueFormatter implements IAxisValueFormatter {
    private final List<Profile> profiles;
    private final BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart, List<Profile> profiles) {
        this.chart = chart;
        this.profiles = profiles;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int xAxisValue = (int) value;
        return profiles.get(xAxisValue).getDate();
    }
}


