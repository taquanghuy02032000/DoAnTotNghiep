package online.javalab.poly.fragments.lesson;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.databinding.FragmentQuizSummaryBinding;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;


public class QuizSummaryFragment extends BaseFragment<SharedTopicQuizViewModel, FragmentQuizSummaryBinding> {
    int myMark = 0;

    private boolean isProgressSuccess = false;

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quiz_summary;
    }

    @Override
    public void backFromAddFragment() {

    }

    @Override
    public boolean backPressed() {
        viewModel.getCorrectedCount().setValue(0);
        viewModel.isUpdate().setValue(true);
        mViewController.backTwoStepFromAddFragment(null);
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initView() {
        binding.setLifecycleOwner(this);
        viewModel.getMark().observe(getViewLifecycleOwner(), mark -> {
            myMark = Math.round(mark);
            binding.setMark(myMark);
            binding.progressQuizMarked.setProgress(myMark, true);
            if (myMark >= 80) {
                binding.setMessage(Define.Message.MES_FINISH_QUIZ_1);
            } else if (70 >= myMark && myMark >= 50) {
                binding.setMessage(Define.Message.MES_FINISH_QUIZ_2);
            } else {
                binding.setMessage(Define.Message.MES_FINISH_QUIZ_3);
            }
            updateProgress();

        });

    }

    @Override
    public void initData() {
        int correctTotal = viewModel.getCorrectedCount().getValue() != null ? viewModel.getCorrectedCount().getValue() : 0;
        int questionTotal = viewModel.getQuestionList().getValue() != null ? viewModel.getQuestionList().getValue().size() : 0;
        int incorrectTotal = questionTotal - correctTotal;
        binding.tvSumTotalQuestion.setText(String.valueOf(questionTotal));
        binding.tvSumCorrectedCount.setText(String.valueOf(correctTotal));
        binding.tvSumIncorrectCount.setText(String.valueOf(incorrectTotal));
        viewModel.setMark(questionTotal, correctTotal);
        viewModel.getProgressResponse().observe(getViewLifecycleOwner(), this::handleObjectResponse);
    }

    @Override
    public void initListeners() {
        binding.btnSumExit.setOnClickListener(v -> {
            viewModel.getCorrectedCount().setValue(0);
            mViewController.backTwoStepFromAddFragment(null);

        });
    }

    @Override
    protected <U> void getObjectResponse(U data) {
        if (data != null && data instanceof LearnProgress) {
            viewModel.getProgressUpdateData().setValue((LearnProgress) data);
            viewModel.isUpdate().setValue(true);
            isProgressSuccess = true;
        }
    }

    private void updateProgress() {
        if (!DeviceUtil.hasConnection(requireActivity())) {
            CommonExt.toast(requireActivity(), Define.Message.ASK_TO_CONNECT);
        }
        ProgressRequest progressRequest = new ProgressRequest();
        progressRequest.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
        Lesson lesson = viewModel.getLessonData().getValue();
        if (lesson != null) {
            progressRequest.setLessonId(lesson.getId());
            progressRequest.setStatus(1);
            progressRequest.setQuizStatus(1);
            progressRequest.setQuizMarked(myMark);
            viewModel.fetchCreateOrUpdateProgress(progressRequest);
            viewModel.isUpdate().setValue(true);
        }
    }

}