package online.javalab.poly.fragments;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.databinding.FragmentProfileBinding;
import online.javalab.poly.fragments.profile.DayStreakFragment;
import online.javalab.poly.fragments.profile.TopLessonScoreFragment;
import online.javalab.poly.model.Profile;
import online.javalab.poly.model.TopLesson;
import online.javalab.poly.model.rank.Datum;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.utils.ImageUtils;
import online.javalab.poly.viewmodel.ProfileViewModel;

public class ProfileFragment extends BaseFragment<ProfileViewModel, FragmentProfileBinding> implements SwipeRefreshLayout.OnRefreshListener {
    private List<BarEntry> values;
    private List<Datum> datumList;
    private List<Profile> profiles;
    private List<TopLesson> topLessons;

    @Override
    protected ProfileViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
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
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (signInAccount != null) {
            binding.textProfileName.setText(signInAccount.getDisplayName());
            binding.textProfileEmail.setText(signInAccount.getEmail());
            ImageUtils.loadImage(binding.imageAvatar,
                    Objects.requireNonNull(signInAccount.getPhotoUrl()).toString());
        }
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        values = new ArrayList<>();
        datumList = new ArrayList<>();

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Day Streak"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Top Score Lesson"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    DayStreakFragment fragment = DayStreakFragment.newInstance(values, profiles);
                    getChildFragmentManager().beginTransaction().replace(R.id.viewPager, fragment).commit();
                } else {
                    TopLessonScoreFragment fragment = TopLessonScoreFragment.newInstance(topLessons);
                    getChildFragmentManager().beginTransaction().replace(R.id.viewPager, fragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void initData() {
        viewModel.loadProfileScores(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
        viewModel.loadProfileRank();
        viewModel.loadTopLesson(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));

        viewModel.getProfileScore(DataStorageManager.getStringValue(Define.StorageKey.USER_ID))
                .observe(getViewLifecycleOwner(), this::handleListResponse);
        viewModel.getDatumList()
                .observe(getViewLifecycleOwner(), this::handleData);
        viewModel.getTopLessons()
                .observe(getViewLifecycleOwner(), this::handleTopLessonData);
    }

    private void handleTopLessonData(ListResponse<TopLesson> listResponse) {
        topLessons = new ArrayList<>();
        if (listResponse.getData() != null) {
            topLessons = listResponse.getData();

            TopLessonScoreFragment.newInstance(topLessons);
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleData(ListResponse<Datum> datumListResponse) {
        if (datumListResponse.getData() != null) {
            datumList = datumListResponse.getData();
        }
        if (datumList != null) {
            for (int i = 0; i < datumList.size(); i++) {
                if (datumList.get(i).getId()
                        .equals(DataStorageManager.getStringValue(Define.StorageKey.USER_ID))) {
                    binding.textRank.setText(getResources().getString(R.string.text_rank) + " " + (i + 1));
                    binding.textScore.setText(String.valueOf(datumList.get(i).getMark()));
                }
            }
        }
    }

    @Override
    public void initListeners() {
    }

    @Override
    protected void getListResponse(List<?> data) {
        if (data != null) {
            profiles = (List<Profile>) data;
            Collections.reverse(profiles);
            for (int i = 0; i < profiles.size(); i++) {
                values.add(new BarEntry(i, profiles.get(i).getMark()));
            }

            DayStreakFragment fragment = DayStreakFragment.newInstance(values, profiles);
            getChildFragmentManager().beginTransaction().replace(R.id.viewPager, fragment).commit();
        }
    }

    @Override
    public void onRefresh() {
        if (values != null) {
            values.clear();
        }
        binding.swipeRefreshLayout.setRefreshing(false);
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
        if (!DeviceUtil.hasConnection(requireActivity())) {
            CommonExt.toast(requireActivity(), Define.Message.NO_CONNECT);
        } else {
            viewModel.loadProfileScores(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
            viewModel.loadProfileRank();
            viewModel.loadTopLesson(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
        }
    }
}
