package online.javalab.poly.base.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import online.javalab.poly.base.BaseViewModel;
import online.javalab.poly.dto.network.RequestError;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DialogUtil;


public abstract class BaseFragment<VM extends BaseViewModel, T extends ViewDataBinding> extends Fragment {


    protected ViewModelProvider.Factory viewModelFactory;

    protected T binding;
    protected VM viewModel;
    /**
     * The ViewController for control fragments in an activity
     */
    @Nullable
    protected ViewController mViewController;

    protected abstract VM onCreateViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        binding.setLifecycleOwner(this.getViewLifecycleOwner());
        viewModel = onCreateViewModel();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
        initListeners();
    }

    protected abstract int getLayoutId();

    public void setViewController(ViewController viewController) {
        this.mViewController = viewController;
    }

    public void setData(HashMap<String, Object> data) {
        if (data == null || data.isEmpty()) {
            setArguments(new Bundle());
            return;
        }
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                bundle.putString(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                bundle.putDouble(entry.getKey(), ((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                bundle.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                bundle.putFloat(entry.getKey(), ((Float) entry.getValue()));
            } else if (entry.getValue() instanceof Boolean) {
                bundle.putBoolean(entry.getKey(), ((Boolean) entry.getValue()));
            } else if (entry.getValue() instanceof Parcelable) {
                bundle.putParcelable(entry.getKey(), ((Parcelable) entry.getValue()));
            } else if (entry.getValue() instanceof String[]) {
                bundle.putStringArray(entry.getKey(), (String[]) entry.getValue());
            } else if (entry.getValue() instanceof ArrayList) {
                if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof String) {
                    bundle.putStringArrayList(entry.getKey(), (ArrayList<String>) entry.getValue());
                } else if (((ArrayList) entry.getValue()).size() > 0 && ((ArrayList) entry.getValue()).get(0) instanceof Parcelable) {
                    bundle.putParcelableArrayList(entry.getKey(), (ArrayList<? extends Parcelable>) entry.getValue());
                }
            }
        }
        setArguments(bundle);
    }

    protected void handleListResponse(ListResponse<?> response) {
        if (response == null) return;
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
                DialogUtil.getInstance(getContext()).show();
                break;
            case Define.ResponseStatus.SUCCESS:
                getListResponse(response.getData());
                DialogUtil.getInstance(getContext()).hidden();
                break;
            case Define.ResponseStatus.ERROR:
                handleNetworkError(response.getError(), true);
                DialogUtil.getInstance(getContext()).hidden();
        }
    }

    protected void handleLoadMoreResponse(ListResponse<?> response, boolean isRefresh, boolean canLoadmore) {
        if (response == null) return;
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
//                DialogUtil.getInstance(getContext()).show();
                break;
            case Define.ResponseStatus.SUCCESS:
                getListResponse(response.getData(), isRefresh, canLoadmore);
                DialogUtil.getInstance(getContext()).hidden();
                break;
            case Define.ResponseStatus.ERROR:
                handleNetworkError(response.getError(), true);
                DialogUtil.getInstance(getContext()).hidden();
        }
    }

    protected <U> void handleObjectResponse(ObjectResponse<U> response) {
        if (response == null) return;
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
                DialogUtil.getInstance(getContext()).show();
                break;
            case Define.ResponseStatus.SUCCESS:
                getObjectResponse(response.getData());
                DialogUtil.getInstance(getContext()).hidden();
                break;
            case Define.ResponseStatus.ERROR:
                handleNetworkError(response.getError(), true);
                DialogUtil.getInstance(getContext()).hidden();

        }
    }

    protected void getListResponse(List<?> data) {

    }

    protected void getListResponse(List<?> data, boolean isRefresh, boolean canLoadmore) {

    }

    protected <U> void getObjectResponse(U data) {

    }

    protected void handleNetworkError(Throwable throwable, boolean isShowDialog) {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).handleNetworkError(throwable, isShowDialog);
        }
    }

    protected boolean avoidDuplicateClick() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).avoidDuplicateClick();
        }

        return false;
    }

    public abstract void backFromAddFragment();

    public abstract boolean backPressed();

    public abstract void initView();

    public abstract void initData();

    public abstract void initListeners();
}
