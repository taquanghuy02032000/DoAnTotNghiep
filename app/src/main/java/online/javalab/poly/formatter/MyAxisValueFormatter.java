package online.javalab.poly.formatter;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

import online.javalab.poly.model.TopLesson;

public class MyAxisValueFormatter implements IAxisValueFormatter {
    private final List<TopLesson> topLessons;

    public MyAxisValueFormatter(List<TopLesson> topLessons) {
        this.topLessons = topLessons;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int xAxisValue = (int) value;
        return topLessons.get(xAxisValue).getTitle();
    }
}
