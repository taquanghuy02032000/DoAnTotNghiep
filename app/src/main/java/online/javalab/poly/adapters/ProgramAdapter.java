package online.javalab.poly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import online.javalab.poly.databinding.ItemProgramBinding;
import online.javalab.poly.model.Program;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.ImageUtils;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    private  List<Program> programs;
    private final ItemClickListener itemClickListener;

    public ProgramAdapter( ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<Program> programs){
        this.programs = programs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProgramBinding binding = ItemProgramBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Program program = programs.get(position);
        holder.binding.textNameProgram.setText(program.getName());
        ImageUtils.loadImage(holder.binding.imageProgram, Define.Api.BASE_URL + program.getImage());
    }

    @Override
    public int getItemCount() {
        if (programs == null) {
            return 0;
        }
        return programs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemProgramBinding binding;

        public ViewHolder(ItemProgramBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Program program = programs.get(getAdapterPosition());
            itemClickListener.onItemClick(program);
        }
    }

    public interface ItemClickListener {
        void onItemClick(Program program);
    }
}
