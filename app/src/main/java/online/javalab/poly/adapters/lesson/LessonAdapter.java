package online.javalab.poly.adapters.lesson;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import online.javalab.poly.R;
import online.javalab.poly.base.adapter.BaseRecyclerViewAdapter;
import online.javalab.poly.databinding.ItemLessonBinding;
import online.javalab.poly.databinding.ItemSubTopicBinding;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.Topic;
import online.javalab.poly.utils.Define;

public class LessonAdapter extends BaseRecyclerViewAdapter<ItemLessonBinding> implements Filterable {

    private List<Lesson> mRootLessonList = new ArrayList<>();
    private List<Lesson> mLessonList = new ArrayList<>();
    private List<LearnProgress> progressList = new ArrayList<>();
    private OnLessonClickListener onLessonClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<Lesson> list) {
        this.mLessonList = list;
        this.mRootLessonList = list;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProgressListData(List<LearnProgress> list) {
        this.progressList = list;

        if (mLessonList != null) {
            for (LearnProgress progress : list) {
                for (Lesson lesson : mLessonList) {
                    if (progress.getLessonId().equals(lesson.getId())) {
                        lesson.setProgress(progress);
                    }
                }
            }
        }

        notifyDataSetChanged();
    }

    public void setOnLessonClickListener(OnLessonClickListener onLessonClickListener) {
        this.onLessonClickListener = onLessonClickListener;
    }

    @Override
    protected int layoutResId() {
        return R.layout.item_lesson;
    }

    @Override
    protected BaseViewHolder solvedOnCreateViewHolder(ItemLessonBinding binding) {
        return new LessonViewHolder(binding);
    }

    @Override
    protected void solvedOnBindViewHolder(BaseViewHolder holder, int position) {
        if (mLessonList == null) return;
        LessonViewHolder viewHolder = (LessonViewHolder) holder;
        Lesson lesson = mLessonList.get(position);
        viewHolder.bindData(lesson);
        viewHolder.setOnLessonClickListener(onLessonClickListener);
    }

    @Override
    public int getItemCount() {
        if (mLessonList != null) {
            return mLessonList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                FilterResults filterResults = new FilterResults();
                List<Lesson> list = new ArrayList<>();
                if (TextUtils.isEmpty(strSearch)) {
                    list = mRootLessonList;
                } else {
                    for (Lesson lesson : mRootLessonList) {
                        if (lesson.getTitle().trim().toLowerCase().contains(strSearch.trim().toLowerCase())) {
                            list.add(lesson);
                        }
                    }
                }
                filterResults.values = list;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null) {
                    mLessonList = (List<Lesson>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }

    public interface OnLessonClickListener {
        void onItemClick(Lesson lesson);
    }

    class LessonViewHolder extends BaseViewHolder<Lesson> {
        ItemLessonBinding binding;
        OnLessonClickListener onLessonClickListener;
        LearnProgress progress;

        protected LessonViewHolder(@NonNull ItemLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        @Override
        protected void bindData(Lesson data) {
            binding.setLesson(data);

            if (data.getProgress() != null) {
                if (data.getProgress().getCompleteList() != null) {
                    binding.setComplete(data.getProgress().getCompleteList().size());
                    for (String completedId : data.getProgress().getCompleteList()) {
                        for (Topic topic : data.getTopics()) {
                            if (completedId.equals(topic.getId())) {
                                topic.setCompleted(true);
                            }
                        }
                    }
                }

                if (data.getProgress().getStatus() == Define.Status.STATUS_NOT_STARTED) {
                    binding.setButtonTitle(Define.ButtonConstants.START);

                } else if (data.getProgress().getStatus() == Define.Status.STATUS_LEARNING
                        || data.getProgress().getQuizStatus() < Define.Status.STATUS_COMPLETED) {
                    binding.setButtonTitle(Define.ButtonConstants.CONTINUES);

                } else if (data.getProgress().getStatus() == Define.Status.STATUS_COMPLETED
                        && data.getProgress().getQuizStatus() < Define.Status.STATUS_COMPLETED) {
                    binding.setButtonTitle(Define.ButtonConstants.START_QUIZ);

                } else if (data.getProgress().getStatus() == Define.Status.STATUS_COMPLETED
                        && data.getProgress().getQuizStatus() == Define.Status.STATUS_COMPLETED) {
                    binding.setButtonTitle(Define.ButtonConstants.REVIEW);
                }


            } else {
                binding.setButtonTitle(Define.ButtonConstants.START);
                binding.setComplete(0);
            }

            TopicAdapter<ItemSubTopicBinding> adapter = new TopicAdapter<>(ItemSubTopicBinding.class);
            adapter.setListData(data.getTopics());
            binding.rcvLessonSubTopics.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false));
            binding.rcvLessonSubTopics.setAdapter(adapter);

            binding.btnLessonStart.setOnClickListener(v -> {
                onItemClick(data, onLessonClickListener);
            });

            binding.imgExpandCollapse.setOnClickListener(v -> {
                handleExpandCollapse(data);

            });
            itemView.setOnClickListener(view -> {
                handleExpandCollapse(data);
            });

        }

        public void setProgress(LearnProgress progress) {
            this.progress = progress;
        }


        public void setOnLessonClickListener(OnLessonClickListener onLessonClickListener) {
            this.onLessonClickListener = onLessonClickListener;
        }

        private void onItemClick(Lesson data, OnLessonClickListener onLessonClickListener) {
            if (onLessonClickListener != null) {
                onLessonClickListener.onItemClick(data);
            }
        }

        private void handleExpandCollapse(Lesson lesson) {
            lesson.setExpand(!lesson.isExpand());
            LessonAdapter.this.notifyItemChanged(getAdapterPosition());
        }
    }
}
