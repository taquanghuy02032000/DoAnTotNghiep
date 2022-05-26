package online.javalab.poly.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import online.javalab.poly.activitys.program.ProgramDetailActivity;
import online.javalab.poly.adapters.ProgramAdapter;
import online.javalab.poly.databinding.FragmentProgramBinding;
import online.javalab.poly.dto.JavaLabServer;
import online.javalab.poly.interfaces.JavaLabApi;
import online.javalab.poly.model.Program;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProgramFragment extends Fragment implements ProgramAdapter.ItemClickListener {
    public static final String BUNDLE_PROGRAM = "BUNDLE_PROGRAM";
    private FragmentProgramBinding binding;
    private List<Program> programs;
    private ProgramAdapter programAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProgramBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        programAdapter = new ProgramAdapter(this);
        binding.recyclerProgram.setAdapter(programAdapter);
        binding.recyclerProgram.addOnItemTouchListener(CommonExt.onRecyclerViewItemTouchListener(1));

        if (DeviceUtil.hasConnection(requireActivity())) {
            getData();
        }
        binding.programRefreshLayout.setOnRefreshListener(() -> {
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), Define.Message.NO_CONNECT);
                binding.programRefreshLayout.setRefreshing(false);
            } else getData();
        });
    }

    private void getData() {
        JavaLabApi javaLabApi = JavaLabServer.getJavaLab().create(JavaLabApi.class);
        javaLabApi.getPrograms().enqueue(new Callback<List<Program>>() {
            @Override
            public void onResponse(@NonNull Call<List<Program>> call, @NonNull Response<List<Program>> response) {
                if (response.isSuccessful()) {
                    programs = response.body();
                    if (programs != null)
                        programAdapter.setData(programs);

                    binding.programRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Program>> call, @NonNull Throwable t) {
                ToastUtils.showShort(t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onItemClick(Program program) {
        Intent intent = new Intent(getContext(), ProgramDetailActivity.class);
        intent.putExtra(BUNDLE_PROGRAM, program);
        startActivity(intent);
    }
}
