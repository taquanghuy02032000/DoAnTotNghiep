package online.javalab.poly.fragments.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import online.javalab.poly.R;

public class MyMarkerView extends MarkerView {

    private final TextView tvContent;
    private final IAxisValueFormatter xAxisValueFormatter;


    public MyMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter) {
        super(context, R.layout.view_marker_chart);

        tvContent = (TextView) findViewById(R.id.textContent);
        this.xAxisValueFormatter = xAxisValueFormatter;

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null));

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
