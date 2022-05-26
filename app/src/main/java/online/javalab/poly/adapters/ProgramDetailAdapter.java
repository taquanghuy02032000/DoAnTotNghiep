package online.javalab.poly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import online.javalab.poly.databinding.ItemProgramDetailBinding;
import online.javalab.poly.model.ProgramDetail;

public class ProgramDetailAdapter extends RecyclerView.Adapter<ProgramDetailAdapter.ViewHolder> {

    private final List<ProgramDetail> examples;
    private final ItemClickListener itemClickListener;

    public ProgramDetailAdapter(List<ProgramDetail> examples, ItemClickListener itemClickListener) {
        this.examples = examples;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProgramDetailBinding binding = ItemProgramDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.textExample.setText(examples.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (examples == null){
            return 0;
        }
        return examples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemProgramDetailBinding binding;
        public ViewHolder(ItemProgramDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
