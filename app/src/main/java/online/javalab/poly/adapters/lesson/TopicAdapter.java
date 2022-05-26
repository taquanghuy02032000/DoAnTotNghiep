package online.javalab.poly.adapters.lesson;

import android.annotation.SuppressLint;

import androidx.databinding.ViewDataBinding;

import java.util.ArrayList;
import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.base.adapter.BaseRecyclerViewAdapter;
import online.javalab.poly.databinding.ItemSubTopicBinding;
import online.javalab.poly.databinding.ItemTopicBinding;
import online.javalab.poly.model.lesson.Topic;

public class TopicAdapter<B extends ViewDataBinding> extends BaseRecyclerViewAdapter<B> {

    private List<Topic> topicList = new ArrayList<>();
    int viewType = 0;
    private Class<B> classType ;

    private OnTopicItemClickListener onItemClickListener;

    public TopicAdapter(Class<B> classType) {
        this.classType= classType;
    }

    public TopicAdapter(Class<B> classType, OnTopicItemClickListener onItemClickListener) {
        this.classType = classType;
        this.onItemClickListener = onItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<Topic> topicList) {
        this.topicList = topicList;
        notifyDataSetChanged();
    }

    @Override
    protected int layoutResId() {
        if (classType.isAssignableFrom(ItemTopicBinding.class))
            return R.layout.item_topic;
        else if (classType.isAssignableFrom(ItemSubTopicBinding.class))
            return R.layout.item_sub_topic;
        return 0;
    }

    @Override
    protected BaseViewHolder solvedOnCreateViewHolder(B binding) {
        if (classType.isAssignableFrom(ItemTopicBinding.class)){
            return new TopicViewHolder((ItemTopicBinding) binding);
        }else if (classType.isAssignableFrom(ItemSubTopicBinding.class)){
            return new SubTopicViewHolder((ItemSubTopicBinding) binding);
        }
        return null;
    }

    @Override
    protected void solvedOnBindViewHolder(BaseViewHolder holder, int position) {
        if (topicList == null || topicList.isEmpty()) return;
        if (classType.isAssignableFrom(ItemTopicBinding.class)){
            ((TopicViewHolder) holder).bindData(topicList.get(position));
            ((TopicViewHolder) holder).setOnTopicItemClickListener(onItemClickListener);
        }else if (classType.isAssignableFrom(ItemSubTopicBinding.class)){
            ((SubTopicViewHolder) holder).bindData(topicList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (topicList != null)
            return topicList.size();
        return 0;
    }

   public interface OnTopicItemClickListener {
        void onItemClick(Topic topic,int position);
    }

    static class TopicViewHolder extends BaseViewHolder<Topic> {
        ItemTopicBinding binding;
        OnTopicItemClickListener onTopicItemClickListener;
        Topic topic;
        public TopicViewHolder(ItemTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(view -> {
                onTopicItemClickListener.onItemClick(topic,getAdapterPosition());
            });
        }

        @Override
        protected void bindData(Topic data) {
            binding.setTopic(data);
            this.topic = data;
        }

        public void setOnTopicItemClickListener(OnTopicItemClickListener onTopicItemClickListener){
            this.onTopicItemClickListener = onTopicItemClickListener;
        }
    }

    static class SubTopicViewHolder extends BaseViewHolder<Topic> {
        ItemSubTopicBinding binding;


        public SubTopicViewHolder(ItemSubTopicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        protected void bindData(Topic data) {
            binding.setTopic(data);
            binding.setStt(getAdapterPosition()+1);
        }
    }

}
