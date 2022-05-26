package online.javalab.poly.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.adapters.RankAdapter;
import online.javalab.poly.dto.JavaLabServer;
import online.javalab.poly.interfaces.JavaLabApi;
import online.javalab.poly.model.rank.Datum;
import online.javalab.poly.model.rank.Ratings;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JavaLabApi mJavaLabApi;
    private List<Datum> mRatingsList;
    private RecyclerView mRecyclerViewRank;
    private RankAdapter mRankAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankFragment newInstance() {
        RankFragment fragment = new RankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mRatingsList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData(view);
    }

    private void initView(View view) {
        mRecyclerViewRank = view.findViewById(R.id.fg_rank_recycleview);
        mSwipeRefreshLayout = view.findViewById(R.id.rank_swipeRefreshLayout);
        mRecyclerViewRank.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewRank.setHasFixedSize(true);
        mRecyclerViewRank.addOnItemTouchListener(CommonExt.onRecyclerViewItemTouchListener(1));
        mRankAdapter = new RankAdapter();
        mRecyclerViewRank.setAdapter(mRankAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initData(View view) {
        if (DeviceUtil.hasConnection(requireActivity())) {
            callRankApi(view);
        }
    }

    private void callRankApi(View view) {
        mJavaLabApi = JavaLabServer.getJavaLab().create(JavaLabApi.class);
        mJavaLabApi.getRatings().enqueue(new Callback<Ratings>() {
            @Override
            public void onResponse(Call<Ratings> call, Response<Ratings> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        mRatingsList = response.body().getData();
                        mRankAdapter.setData(mRatingsList);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<Ratings> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (!DeviceUtil.hasConnection(requireActivity())) {
            CommonExt.toast(requireActivity(), Define.Message.NO_CONNECT);
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        callRankApi(requireView());
    }
}
