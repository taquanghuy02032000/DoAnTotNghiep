package online.javalab.poly.fragments.lesson;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.databinding.FragmentDetailTopicBinding;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.model.lesson.Topic;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;

public class DetailTopicFragment extends BaseFragment<SharedTopicQuizViewModel, FragmentDetailTopicBinding> {

    private Topic topic;
    private ProgressRequest progressRequest;
    private boolean isLastElement = false;

    @Override
    public void initView() {
        binding.setLifecycleOwner(this);
    }

    @Override
    public void initData() {
        progressRequest = new ProgressRequest();
        progressRequest.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
        viewModel.getTopicData().observe(getViewLifecycleOwner(), mTopic -> {
            if (mTopic == null) return;
            topic = mTopic;
            progressRequest.setLessonId(topic.getLessonId());
            progressRequest.setTopicId(topic.getId());
            binding.setTopic(topic);
            viewModel.checkTopicIndex(topic);
        });

        viewModel.isLastTopic().observe(getViewLifecycleOwner(), isLast -> {
            if (isLast == null) return;
            if (!isLast) {
                progressRequest.setStatus(Define.Status.STATUS_LEARNING);
            } else {
                binding.setButtonTitle(Define.ButtonConstants.FINISH);
                progressRequest.setStatus(Define.Status.STATUS_COMPLETED);
            }
            isLastElement = isLast;
        });

        viewModel.getProgressResponse().observe(getViewLifecycleOwner(), this::handleObjectResponse);
    }

    @Override
    public void initListeners() {
        binding.btnNextTopic.setOnClickListener(v -> {
            if (topic == null) return;
            if (!topic.isCompleted()) {
                if (!DeviceUtil.hasConnection(requireActivity())) {
                    CommonExt.toast(requireActivity(), Define.Message.ASK_TO_CONNECT);
                }else {
                    viewModel.fetchCreateOrUpdateProgress(progressRequest);
                }
            } else {
                if (progressRequest.getStatus() < 1)
                    viewModel.nextTopic(topic);
                else {
                    assert mViewController != null;
                    mViewController.backFromAddFragment(null);
                }
            }
        });
    }

    @Override
    protected <U> void getObjectResponse(U data) {
        if (data != null) {
            if (data instanceof LearnProgress) {
                viewModel.getProgressUpdateData().setValue((LearnProgress) data);
                viewModel.isUpdate().setValue(true);
                if (progressRequest.getStatus() < 1) {
                    viewModel.nextTopic(topic);
                } else {
                    assert mViewController != null;
                   backPressed();
                }
            }
        }
    }

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detail_topic;
    }

    @Override
    public void backFromAddFragment() {
    }

    @Override
    public boolean backPressed() {
        clearData();
        mViewController.backFromAddFragment(null);
        return false;
    }

    private void clearData() {
        viewModel.getTopicData().setValue(null);
        viewModel.getProgressResponse().setValue(null);
    }


}
