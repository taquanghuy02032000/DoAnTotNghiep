package online.javalab.poly.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import javax.security.auth.callback.Callback;

import online.javalab.poly.R;
import online.javalab.poly.base.base.RecyclerViewAdapter;
import online.javalab.poly.databinding.ItemTabFilterLessonBinding;
import online.javalab.poly.interfaces.MyCallBack;
import online.javalab.poly.interfaces.OnTabLessonSelectedListener;
import online.javalab.poly.model.lesson.FilterTabLesson;

public class TabLessonAdapter extends RecyclerViewAdapter<ItemTabFilterLessonBinding> {
    private OnTabLessonSelectedListener onTabLessonSelectedListener;
    private final int selectedColor = getContext().getColor(R.color.white);
    private final int unselectColor = getContext().getColor(R.color.material_500);

    public void setOnTabSelected(OnTabLessonSelectedListener onTabLessonSelectedListener) {
        this.onTabLessonSelectedListener = onTabLessonSelectedListener;
    }

    public TabLessonAdapter(Context context, boolean enableSelectedMode) {
        super(context, enableSelectedMode);

    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ItemTabFilterLessonBinding binding, ViewGroup parent) {
        return new TabLessonViewHolder(binding);
    }

    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        TabLessonViewHolder viewHolder = (TabLessonViewHolder) holder;
        FilterTabLesson item = getItem(position, FilterTabLesson.class);
        if (item == null) return;
        viewHolder.bind(item);
        //
        viewHolder.setOnTabSelected(onTabLessonSelectedListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_tab_filter_lesson;
    }

    class TabLessonViewHolder extends NormalViewHolder<FilterTabLesson> {
        private ItemTabFilterLessonBinding binding;
        private OnTabLessonSelectedListener onTabLessonSelectedListener;

        public TabLessonViewHolder(ItemTabFilterLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            onItemSelected();
        }

        @Override
        public void bind(FilterTabLesson data) {
            binding.setFilter(data);
            if (!isItemSelected(getAdapterPosition())) {
                binding.tvTabTitle.setBackgroundResource(R.drawable.custom_bg_tap_lesson_unselect);
                binding.tvTabTitle.setTextColor(unselectColor);
            } else {
                binding.tvTabTitle.setBackgroundResource(R.drawable.custom_bg_tap_lesson_selected);
                binding.tvTabTitle.setTextColor(selectedColor);
            }
        }
        //
        public void onItemSelected() {
            itemView.setOnClickListener(v -> {
                if (!isItemSelected(getAdapterPosition())) {
                    clearAllSelectedItems();
                    setSelectedItem(getAdapterPosition(), true);
                    if (onTabLessonSelectedListener != null)
                        onTabLessonSelectedListener.onTapSelected(getItem(getAdapterPosition(), FilterTabLesson.class), getAdapterPosition());
                }
            });
        }
        //
        public void setOnTabSelected(OnTabLessonSelectedListener onTabLessonSelectedListener) {
            this.onTabLessonSelectedListener = onTabLessonSelectedListener;
        }
    }
}
