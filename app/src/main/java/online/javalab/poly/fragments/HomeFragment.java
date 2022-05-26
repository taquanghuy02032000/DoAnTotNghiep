package online.javalab.poly.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.activitys.lesson.TopicsContainerActivity;
import online.javalab.poly.adapters.lesson.LessonAdapter;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.databinding.FragmentHomeBinding;
import online.javalab.poly.model.lesson.FilterTabLesson;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.viewmodel.LessonViewModel;

public class HomeFragment extends BaseFragment<LessonViewModel, FragmentHomeBinding> {

    private LessonAdapter adapter;
    private Lesson selectedLesson = null;
    private boolean isStartToLearn = false;
    private final static int MAIN_RESULT_CODE = 111;
    private static int currentFilter = Define.Tab.TAB_ID_ALL;

    @Override
    protected LessonViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(LessonViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void backFromAddFragment() {

    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void initView() {
        initRecyclerView();
        binding.refreshLessonLayout.setColorSchemeColors(
                requireActivity().getResources().getIntArray(R.array.rainbow)
        );
    }

    @Override
    public void initData() {
        if (!DeviceUtil.hasConnection(requireActivity())) {
           CommonExt.toast(requireActivity(), Define.Message.NO_CONNECT);
        }else {
            viewModel.fetchLessonList(true);
        }
        viewModel.getLessonListResponse().observe(getViewLifecycleOwner(), this::handleListResponse);
        viewModel.getLessonListData().observe(getViewLifecycleOwner(), lessonList -> {
            if (lessonList != null) {
                viewModel.setOnFilterLesson(currentFilter);
            }
        });
        viewModel.getProgressListResponse().observe(getViewLifecycleOwner(), this::handleListResponse);
        viewModel.getProgressResponse().observe(getViewLifecycleOwner(), this::handleObjectResponse);
        viewModel.getListLessonFilter().observe(getViewLifecycleOwner(), lessons -> {
            adapter.setListData(lessons);
        });
    }

    @Override
    protected void getListResponse(List<?> data) {
        if (data == null || data.isEmpty()) return;
        if (data.get(0) instanceof Lesson) {
            currentFilter = Define.Tab.TAB_ID_ALL;
            binding.vTapLesson.refreshTabFilter();
            viewModel.getLessonListData().setValue((List<Lesson>) data);

        } else if (data.get(0) instanceof LearnProgress) {
            viewModel.handleProgressResponse((List<LearnProgress>) data);
        }

        binding.rcvHomeLesson.scrollToPosition(0);

        if (binding.refreshLessonLayout.isRefreshing()) {
            binding.refreshLessonLayout.setRefreshing(false);
        }

        if (!TextUtils.isEmpty(binding.edtSearchLesson.getText())) {
            binding.edtSearchLesson.setText("");
        }

    }

    private void initRecyclerView() {
        adapter = new LessonAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        binding.rcvHomeLesson.setLayoutManager(layoutManager);
        binding.rcvHomeLesson.setHasFixedSize(true);
        binding.rcvHomeLesson.setAdapter(adapter);
        binding.rcvHomeLesson.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireActivity(), R.anim.anim_layout_item_left_to_right));
        binding.rcvHomeLesson.addOnItemTouchListener(CommonExt.onRecyclerViewItemTouchListener(1));
    }


    private TextWatcher onSearch() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.rcvHomeLesson.scrollToPosition(0);
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initListeners() {
        adapter.setOnLessonClickListener(this::startLesson);
        binding.refreshLessonLayout.setOnRefreshListener(() -> {
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), Define.Message.NO_CONNECT);
                binding.refreshLessonLayout.setRefreshing(false);
            }else {
                viewModel.fetchLessonList(false);
            }
            DeviceUtil.hideSoftKeyboard(requireActivity());
            if (!TextUtils.isEmpty(binding.edtSearchLesson.getText())) {
                binding.edtSearchLesson.setText("");
            }
        });

        binding.edtSearchLesson.setOnFocusChangeListener((v, hasFocus) -> {
            binding.rcvHomeLesson.scrollToPosition(0);
            if (!hasFocus) {
                DeviceUtil.hideSoftKeyboard(requireActivity());
            }
        });

        binding.vgParentLesson.setOnTouchListener((v, event) -> {
            if (!(v instanceof EditText)) {
                DeviceUtil.hideSoftKeyboard(requireActivity());
            }
            return false;
        });

        binding.edtSearchLesson.addTextChangedListener(onSearch());
        binding.edtSearchLesson.setOnTouchListener((v, event) -> {
            refreshFilter();
            return false;
        });

        binding.vTapLesson.setOnTapSelectionChangedListener((data, position) -> {
            if (data != null) {
                filterLesson(data, position);
                binding.rcvHomeLesson.scrollToPosition(0);
                binding.vTapLesson.getBinding().rcvTapFilter.scrollToPosition(position);
            }
        });
    }

    private void filterLesson(FilterTabLesson data, int position) {
        if (data ==null) return;
        currentFilter = data.getId();
        viewModel.setOnFilterLesson(data.getId());
    }

    private void refreshFilter(){
        currentFilter = Define.Tab.TAB_ID_ALL;
        viewModel.setOnFilterLesson(currentFilter);
        binding.vTapLesson.refreshTabFilter();
    }

    @Override
    protected <U> void getObjectResponse(U data) {
        if (data != null && data instanceof LearnProgress) {
            LearnProgress progress = (LearnProgress) data;
            if (selectedLesson == null) return;
            if (isStartToLearn) {
                selectedLesson.setProgress(progress);
                startActivity(selectedLesson);
            }
        }
    }

    private void startLesson(Lesson lesson) {
        if (lesson == null) return;
        viewModel.getSelectedLesson().setValue(lesson);
        isStartToLearn = true;
        ProgressRequest request = new ProgressRequest();
        request.setLessonId(lesson.getId());
        request.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));

        if (lesson.getProgress() == null) {
            selectedLesson = lesson;
            request.setStatus(-1);
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), "No Internet Connection");
            }else {
                viewModel.fetchCreateOrUpdateProgress(request);
            }
        } else {
            startActivity(lesson);
        }
    }

    private void startActivity(Lesson lesson) {
        Gson gson = new Gson();
        Intent intent = new Intent(requireActivity(), TopicsContainerActivity.class);
        Bundle bundle = new Bundle();
        String lessonJson = gson.toJson(lesson);
        bundle.putString(Define.BundleKeys.KEY_LESSON, lessonJson);
        intent.putExtras(bundle);
        startActivityForResult(intent, MAIN_RESULT_CODE);
        isStartToLearn = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String strLesson = (String) data.getExtras().get(Define.BundleKeys.KEY_LESSON_RETURN);
            boolean isUpdate = (boolean) data.getExtras().get(Define.BundleKeys.KEY_IS_UPDATE);
            Lesson lesson = new Gson().fromJson(strLesson, Lesson.class);
            if (isUpdate) {
                viewModel.setLessonListData(viewModel.getSelectedLesson().getValue(), lesson);
            }
        }
    }

    private ActivityResultLauncher<Intent> onActivityResultLauncher() {
        return registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String strLesson = (String) data.getExtras().get(Define.BundleKeys.KEY_LESSON_RETURN);
                            boolean isUpdate = (boolean) data.getExtras().get(Define.BundleKeys.KEY_IS_UPDATE);
                            Lesson lesson = new Gson().fromJson(strLesson, Lesson.class);
                            if (isUpdate) {
                                viewModel.setLessonListData(viewModel.getSelectedLesson().getValue(), lesson);
                            }
                        }

                    }
                });
    }


}