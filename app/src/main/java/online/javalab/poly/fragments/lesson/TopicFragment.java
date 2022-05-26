package online.javalab.poly.fragments.lesson;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import online.javalab.poly.R;
import online.javalab.poly.activitys.lesson.TopicsContainerActivity;
import online.javalab.poly.adapters.lesson.TopicAdapter;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.databinding.FragmentTopicBinding;
import online.javalab.poly.databinding.ItemTopicBinding;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.model.lesson.Question;
import online.javalab.poly.model.lesson.Quiz;
import online.javalab.poly.model.lesson.Topic;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;


public class TopicFragment extends BaseFragment<SharedTopicQuizViewModel, FragmentTopicBinding> {

    private TopicAdapter<ItemTopicBinding> adapter;
    private Lesson lesson = null;
    private List<Topic> topicList = new ArrayList<>();
    private List<Question> questionList = null;
    private Quiz quiz = null;
    private ProgressRequest progressRequest;
    boolean isUpdate = false;

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    public void initData() {
        progressRequest = new ProgressRequest();
        progressRequest.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
        viewModel.isUpdate().setValue(false);
        viewModel.getLessonData().observe(getViewLifecycleOwner(), mLesson -> {
            lesson = mLesson;
            if (lesson == null) return;
            binding.setLesson(lesson);
            progressRequest.setLessonId(mLesson.getId());
            if (lesson.getTopics() != null && !lesson.getTopics().isEmpty()) {
                topicList = lesson.getTopics();
                viewModel.getTopicListData().setValue(topicList);
            }
            if (lesson.getQuiz() != null) {
                quiz = lesson.getQuiz();
                questionList = quiz.getQuestions();
                viewModel.getQuiz().setValue(quiz);
                viewModel.getQuestionList().setValue(quiz.getQuestions());
                binding.vgQuizContainer.setVisibility(View.VISIBLE);
                binding.setQuiz(quiz);
                binding.tvQuizTotalQuestion.setText("Questions: " + questionList.size());
            }else binding.vgQuizContainer.setVisibility(View.GONE);

            if (lesson.getProgress() != null) {
                viewModel.getProgressData().setValue(lesson.getProgress());
            }

            viewModel.getTopicListData().observe(getViewLifecycleOwner(), topics -> {
                adapter.setListData(topicList);
            });
        });

        viewModel.getProgressData().observe(getViewLifecycleOwner(), progress -> {
            if (progress != null ) {
                if(progress.getCompleteList() != null){
                    binding.setComplete(progress.getCompleteList().size());
                }else binding.setComplete(0);

                if(progress.getStatus() <1){
                   setEnableQuiz(false);
                }else {
                    setEnableQuiz(true);
                }
                binding.setMarks(progress.getQuizMarked());
            } else {
                setEnableQuiz(false);
                binding.setMarks(0);
                binding.setComplete(0);
            }
        });

        viewModel.getProgressUpdateData().observe(getViewLifecycleOwner(),progress -> {

        });

        viewModel.isUpdate().observe(getViewLifecycleOwner(), isUpdated -> {
            if (isUpdated != null) {
                isUpdate = isUpdated;
                if (isUpdated) {
                    LearnProgress progress = viewModel.getProgressUpdateData().getValue();
                    if (progress != null) {
                        lesson.setProgress(progress);
//                        if (progress.getStatus()==Define.Status.STATUS_COMPLETED){
//                            lesson.setLearnCount(lesson.getLearnCount()+1);
//                        }
                    }
                    binding.setLesson(lesson);
                    viewModel.getLessonData().setValue(lesson);
                }
            }else {
                isUpdate = false;
            }
        });
    }

    @Override
    public void initListeners() {
        binding.btnStartQuiz.setOnClickListener(v -> {
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), "No Internet Connection");
            }else {
                mViewController.addFragment(QuizFragment.class, null);
            }
        });

    }

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_topic;
    }

    @Override
    public void backFromAddFragment() {
        ((TopicsContainerActivity) requireActivity()).setActionBarTitle(lesson.getTitle() != null ? lesson.getTitle() : "");
    }

    @Override
    public boolean backPressed() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Define.BundleKeys.KEY_LESSON_RETURN, new Gson().toJson(lesson));
        resultIntent.putExtra(Define.BundleKeys.KEY_IS_UPDATE, isUpdate);
        if (isUpdate){
            requireActivity().setResult(Activity.RESULT_OK, resultIntent);
        }
        return true;
    }

    @Override
    public void initView() {
        binding.setLifecycleOwner(this);
        initRecyclerView();
        initListeners();
    }

    private void initRecyclerView() {
        adapter = new TopicAdapter<>(ItemTopicBinding.class, this::startToLearn);
        binding.rcvTopic.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false));
        binding.rcvTopic.setAdapter(adapter);
    }

    private void startToLearn(Topic topic, int position) {
        if (topic==null) return;
        if (topic.isEnabled()){
            viewModel.getTopicData().setValue(topic);
            mViewController.addFragment(DetailTopicFragment.class,null );
        }else {
            CommonExt.toast(requireActivity(),requireActivity().getString(R.string.toast_learn_topic_in_order), Toast.LENGTH_LONG);
        }
    }

  @SuppressLint("UseCompatLoadingForDrawables")
  private void setEnableQuiz(boolean enable){
      if(!enable){
          binding.setEnableQuiz(false);
          binding.btnStartQuiz.setBackground(requireActivity().getDrawable(R.drawable.custom_button_disabled));
      }else {
          binding.setEnableQuiz(true);
          binding.btnStartQuiz.setBackground(requireActivity().getDrawable(R.drawable.custom_button_enabled));
      }
  }

}