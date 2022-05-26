package online.javalab.poly.base.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;



public abstract class BaseRecyclerViewAdapter<B extends ViewDataBinding> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder>{

    protected abstract int layoutResId();

    protected abstract BaseViewHolder solvedOnCreateViewHolder(B binding);

    protected abstract void solvedOnBindViewHolder(BaseViewHolder holder, int position);

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId(), parent, false);
        return solvedOnCreateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        solvedOnBindViewHolder(holder, position);
    }


    public abstract static class BaseViewHolder<U> extends RecyclerView.ViewHolder {

        protected BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        protected abstract void bindData(U data);


    }
}
