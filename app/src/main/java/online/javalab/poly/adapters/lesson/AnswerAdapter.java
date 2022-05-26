package online.javalab.poly.adapters.lesson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import online.javalab.poly.R;
import online.javalab.poly.base.base.RecyclerViewAdapter;
import online.javalab.poly.databinding.ItemAnswerBinding;

public class AnswerAdapter extends RecyclerViewAdapter<ItemAnswerBinding> {

    private final int selectedColor = getContext().getColor(R.color.white);
    private final int unselectColor = getContext().getColor(R.color.material_500);

    public AnswerAdapter(Context context, boolean enableSelectedMode) {
        super(context, enableSelectedMode);
    }

    @Override
    protected RecyclerView.ViewHolder initNormalViewHolder(ItemAnswerBinding binding, ViewGroup parent) {
        return new AnswerViewHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void bindNormalViewHolder(NormalViewHolder holder, int position) {
        AnswerViewHolder viewHolder = (AnswerViewHolder) holder;
        String data= getItem(position, String.class).toString();
        if (data.isEmpty()) return;
        viewHolder.bind(data.trim());
        viewHolder.binding.setIndex(position + 1);
        viewHolder.onItemSelected();
        if (!isItemSelected(viewHolder.getAdapterPosition())) {
            viewHolder.binding.tvAnswer.setBackgroundResource(R.drawable.custom_background_item_primary_disabled);
            viewHolder.binding.tvAnswer.setTextColor(unselectColor);
        }else {
            viewHolder.binding.tvAnswer.setBackgroundResource(R.drawable.custom_background_item_selected);
            viewHolder.binding.tvAnswer.setTextColor(selectedColor);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_answer;
    }


    public class AnswerViewHolder extends NormalViewHolder<String> {
        public ItemAnswerBinding binding;

        public AnswerViewHolder(ItemAnswerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        @Override
        public void bind(String data) {
            if (data!=null || !data.trim().isEmpty()){
                binding.setAnswer(data.trim());
            }
        }

        public void onItemSelected() {
            itemView.setOnClickListener(v -> {
                clearAllSelectedItems();
                if (!isItemSelected(getAdapterPosition())) {
                    setSelectedItem(getAdapterPosition(), true);
                }
            });
        }
    }


}
