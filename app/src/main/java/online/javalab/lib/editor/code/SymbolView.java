package online.javalab.lib.editor.code;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by why on 2017/11/4.
 */
public class SymbolView extends HorizontalScrollView {
    // Context
    private final Context context;
    // TAB
    public static final String TAB = "TAB";
    // Width of each symbol
    private static final int TILE_WIDTH = 100;
    // symbol
    private static final String symbol = "→{}();,%.=\"[]#+-*/<>\\|'&!~?$@:_";
    private int textBackgroundColor = 0xFFEEEEEE;
    // Symbol click event
    private OnSymbolViewClick onSymbolViewClick;
    private LinearLayout linearLayout;

    public SymbolView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SymbolView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SymbolView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        // Cancel scroll bar
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        linearLayout = new LinearLayout(context);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);

        addSymbol();
        this.addView(linearLayout);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addSymbol() {
        linearLayout.removeAllViews();
        final float[] tempPoint = new float[2];
        for (int i = 0; i < SymbolView.symbol.length(); i++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            if (i == 0) {
                textView.setText(TAB);
                textView.setPadding(24, 0, 20, 0);
            } else {
                textView.setText(String.valueOf(SymbolView.symbol.charAt(i)));
            }

            textView.setHeight(ConvertUtils.dp2px(42));
            textView.setFocusable(true);
            textView.setClickable(true);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(18);
            textView.setBackgroundColor(textBackgroundColor);
            textView.setMinWidth(TILE_WIDTH);

            // Set symbol click callback and click to deepen the background
            textView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tempPoint[0] = event.getX();
                        tempPoint[1] = event.getY();
                        textView.setBackgroundColor(Color.GRAY);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // Restore default colors
                        textView.setBackgroundColor(textBackgroundColor);
                        if (Math.abs(event.getX() - tempPoint[0]) < TILE_WIDTH) {
                            if (onSymbolViewClick != null)
                                onSymbolViewClick.onClick(textView, textView.getText().toString());
                        }
                        break;
                }
                return true;
            });
            linearLayout.addView(textView);
        }
    }

    public void setVisible(boolean visible) {
        if (!visible) {
            this.setVisibility(View.GONE);
            return;
        }
        this.setVisibility(View.VISIBLE);
    }

    public void setOnSymbolViewClick(OnSymbolViewClick onSymbolViewClick) {
        this.onSymbolViewClick = onSymbolViewClick;
    }

    public interface OnSymbolViewClick {
        void onClick(View view, String text);
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
        addSymbol();
    }
}
