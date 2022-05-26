package online.javalab.poly.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import online.javalab.poly.adapters.TabLessonAdapter;
import online.javalab.poly.databinding.ViewTabLessonBinding;
import online.javalab.poly.interfaces.OnTabLessonSelectedListener;
import online.javalab.poly.model.lesson.FilterTabLesson;
import online.javalab.poly.utils.CommonExt;

public class TabLessonView extends LinearLayoutCompat {
    private ViewTabLessonBinding binding;
    private Context context;
    private OnTabLessonSelectedListener onTabSelected;
    private TabLessonAdapter adapter;


    public TabLessonView(@NonNull Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TabLessonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TabLessonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        binding = ViewTabLessonBinding.inflate(LayoutInflater.from(context));
        addView(binding.getRoot());
        //
        adapter = new TabLessonAdapter(context, true);
        adapter.refresh(FilterTabLesson.getDefaultTaps());
        adapter.setSelectedItem(0,true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        binding.rcvTapFilter.setLayoutManager(layoutManager);
        binding.rcvTapFilter.smoothScrollToPosition(0);
        binding.rcvTapFilter.setHasFixedSize(true);
        binding.rcvTapFilter.setAdapter(adapter);
        binding.rcvTapFilter.addOnItemTouchListener(CommonExt.onRecyclerViewItemTouchListener(2));
        //
        initListener();
    }

    private void initListener() {

    }

    public void setOnTapSelectionChangedListener(OnTabLessonSelectedListener onTabSelected) {
        this.onTabSelected = onTabSelected;
        adapter.setOnTabSelected(onTabSelected);
    }


    public ViewTabLessonBinding getBinding() {
        return binding;
    }

    public void refreshTabFilter(){
        adapter.clearAllSelectedItems();
        adapter.setSelectedItem(0,true);
        binding.rcvTapFilter.scrollToPosition(0);
    }
}
