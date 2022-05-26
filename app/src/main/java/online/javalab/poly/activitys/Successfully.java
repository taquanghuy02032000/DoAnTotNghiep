package online.javalab.poly.activitys;

import android.os.Handler;

import androidx.lifecycle.ViewModelProvider;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseActivity;
import online.javalab.poly.databinding.ActivitySuccessfullyBinding;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;

public class Successfully extends BaseActivity<SharedTopicQuizViewModel, ActivitySuccessfullyBinding> {

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(SharedTopicQuizViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_successfully;
    }

    @Override
    public int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void initView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
            }
        }, 2000);
    }

    @Override
    public void initData() {

    }
}