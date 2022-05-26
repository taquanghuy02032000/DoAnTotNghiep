package online.javalab.poly.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;

import online.javalab.poly.R;
import online.javalab.poly.SendFAQApi;
import online.javalab.poly.base.base.BaseActivity;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.databinding.ActivitySendFaqactivityBinding;
import online.javalab.poly.dto.MyFirebaseMessagingService;
import online.javalab.poly.model.QA;
import online.javalab.poly.model.SendMail;
import online.javalab.poly.model.ServerRespone;
import online.javalab.poly.model.lesson.Chat;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.utils.DialogUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Todo by CanhNamDinh on: 1/10/2022
 */
public class SendFAQActivity extends BaseActivity<SharedTopicQuizViewModel, ActivitySendFaqactivityBinding> {


    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(SharedTopicQuizViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_faqactivity;
    }

    @Override
    public int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void initView() {
        QA model = (QA) getIntent().getSerializableExtra(Define.BundleKeys.KEY_IS_QA);
        Log.d("TAGGG", new Gson().toJson(model));
        if (model != null) {
            binding.sendfaqActivityTitleEdt.setText(model.getTitle() == null ? "" : model.getTitle());
            binding.sendfaqActivityTitleEdt.setFocusable(false);

        }

        binding.sendfaqActivitySendAppcompatbtn.setOnClickListener(v -> {
            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
            SendMail mail = new SendMail();
            mail.setEmail(signInAccount.getEmail());
            mail.setSubject("Dear\t " + signInAccount.getDisplayName());
            viewModel.sendMailUser(mail.getEmail(), mail.getSubject());
            if (!DeviceUtil.hasConnection(this)) {
                CommonExt.toast(this, Define.Message.ASK_TO_CONNECT);
                return;
            }
            QA qa = new QA();
            if (!TextUtils.isEmpty(binding.sendfaqActivityTitleEdt.getText().toString()) && !TextUtils.isEmpty(binding.sendfaqActivityContentMulti.getText().toString())) {
                qa.setContent(binding.sendfaqActivityContentMulti.getText().toString().trim());
                qa.setTitle(binding.sendfaqActivityTitleEdt.getText().toString().trim());
                qa.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
                qa.setUser(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getEmail());
                qa.setStatus(model == null ? false : model.isStatus());
                qa.setType(model == null ? false : model.isType());
                qa.setIdQuestionId(model == null ? "" : model.getIdQuestionId());
                viewModel.createQA(qa);
                viewModel.creatNotifi(new QA(MyFirebaseMessagingService.getToken(this), "Chào \t" + signInAccount.getDisplayName(), "Cảm ơn bạn đã bổ sung ý kiến"));
            } else {
                Toast.makeText(this, "Mời bạn nhập tiêu đề và nội dung  ", Toast.LENGTH_SHORT).show();
            }
        });


        binding.sendfaqActivityBackImg.setOnClickListener(v -> {
            onBackPressed();
        });
        viewModel.getmFeedBack().observe(this, this::handleResponse);
    }

    @Override
    public void initData() {

    }

    private void handleResponse(ObjectResponse<QA> response) {
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
                DialogUtil.getInstance(getApplicationContext()).show();
                break;
            case Define.ResponseStatus.SUCCESS:
                if (response.getData() != null) {
                    Intent intent = new Intent(SendFAQActivity.this, Successfully.class);
                    startActivity(intent);
                    finish();
                }
                DialogUtil.getInstance(getApplicationContext()).hidden();
                break;
            case Define.ResponseStatus.ERROR:
                DialogUtil.getInstance(getApplicationContext()).hidden();
        }
    }

}