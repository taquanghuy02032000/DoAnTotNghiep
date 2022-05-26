package online.javalab.poly.activitys.lesson;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseActivity;
import online.javalab.poly.databinding.ActivityTopicContainerBinding;
import online.javalab.poly.fragments.lesson.TopicFragment;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DialogUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;

public class TopicsContainerActivity extends BaseActivity<SharedTopicQuizViewModel, ActivityTopicContainerBinding> {

    private Intent resultIntent;

    @Override
    public void initView() {
        resultIntent = new Intent();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String lJson = bundle.getString(Define.BundleKeys.KEY_LESSON);
            Lesson lesson = (Lesson) new Gson().fromJson(lJson, Lesson.class);
            if (lesson != null) {
                viewModel.getLessonData().setValue(lesson);
                setActionBarTitle(lesson.getTitle()!=null? lesson.getTitle() : "");
            }
        }
        DialogUtil.init(this);
        mViewController.addFragment(TopicFragment.class, null);
    }

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(SharedTopicQuizViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_topic_container;
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.topics_container;
    }


    @Override
    public void initData() {

    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(title);
    }

}