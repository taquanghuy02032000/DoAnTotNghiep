package online.javalab.poly.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import online.javalab.poly.MainActivity;
import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseActivity;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.databinding.ActivityLoginBinding;
import online.javalab.poly.dto.MyFirebaseMessagingService;
import online.javalab.poly.model.User;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.utils.DialogUtil;
import online.javalab.poly.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity<LoginViewModel, ActivityLoginBinding> implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;

    private ProgressDialog mProgressDialog;

    @Override
    protected LoginViewModel onCreateViewModel() {
        return new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public int getFragmentContainerId() {
        return 0;
    }

    @Override
    public void initView() {
        initComponents();
        initEvents();
    }

    @Override
    public void initData() {
        viewModel.getUserResponse().observe(this, this::handleObjectResponse);
    }

    @Override
    protected <U> void handleObjectResponse(ObjectResponse<U> response) {
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
                break;
            case Define.ResponseStatus.SUCCESS:
                getObjectResponse(response.getData());
                break;
            case Define.ResponseStatus.ERROR:
                handleErrorResponse(response.getError());
        }
    }

    @Override
    protected <U> void getObjectResponse(U data) {
        User user = (User) data;
        DataStorageManager.putStringValue(Define.StorageKey.USER_ID, user.getId());
        hideProgressDialog();
        DialogUtil.destroyCurrentInstance();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void handleErrorResponse(Throwable throwable) {
        hideProgressDialog();
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                status -> {
                }
        );
        handleNetworkError(throwable, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                firebaseAuthWithGoogle(account);
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        ToastUtils.showShort(R.string.google_play_services_error);
    }

    private void initComponents() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {

            } else {

            }
        };
    }

    private void initEvents() {
        binding.buttonLogin.setOnClickListener(v -> {
            if (!DeviceUtil.hasConnection(this)) {
                CommonExt.toast(this, Define.Message.ASK_TO_CONNECT);
            } else {
                signIn();
            }
        });
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();
        User user = new User();
        user.setGmail(acct.getEmail());
        Log.d("TOKEN", MyFirebaseMessagingService.getToken(LoginActivity.this));
        user.setTokenDevice(MyFirebaseMessagingService.getToken(LoginActivity.this));
        user.setUsername(acct.getDisplayName());
        if (acct.getPhotoUrl() != null) user.setImageUrl(acct.getPhotoUrl().toString());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (!task.isSuccessful()) {
                binding.textError.setTextColor(Color.RED);
                binding.textError.setText(task.getException().getMessage());
            } else {
                getOrCreateUser(user);
            }
            hideProgressDialog();
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void getOrCreateUser(User accInfo) {
        if (!DeviceUtil.hasConnection(this)) {
            CommonExt.toast(this, Define.Message.ASK_TO_CONNECT);
        } else {
            viewModel.getOrCreateNewUser(accInfo);
        }

    }
}
