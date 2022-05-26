package online.javalab.poly.activitys;

import android.content.Intent;

import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseActivity;
import online.javalab.poly.databinding.ActivitySettingBinding;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.Define;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;

public class SettingActivity extends BaseActivity<SharedTopicQuizViewModel, ActivitySettingBinding> {
    private FirebaseAuth mAuth;

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(SharedTopicQuizViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public int getFragmentContainerId() {
        return 0;
    }


    @Override
    public void initView() {
        mAuth = FirebaseAuth.getInstance();

        binding.activitySettingCreateFqaText.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, SendFAQActivity.class));
        });
        binding.swictClick.setOnClickListener(v -> {
            DataStorageManager.putIsCheckNotifi(binding.swictClick.isChecked(), Define.StorageKey.IS_CHECK_NOTIFICATION);
        });
        binding.activitySettingBackImg.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.activitySettingHelpfaqText.setOnClickListener(v -> {
            startActivity(new Intent(SettingActivity.this, HelpFAQActivity.class));
        });


    }

    @Override
    public void initData() {

    }


}