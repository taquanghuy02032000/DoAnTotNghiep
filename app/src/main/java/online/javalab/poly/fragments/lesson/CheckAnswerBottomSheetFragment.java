package online.javalab.poly.fragments.lesson;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import online.javalab.poly.R;
import online.javalab.poly.activitys.SendFAQActivity;
import online.javalab.poly.adapters.lesson.ChatAdapter;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.databinding.BottomSheetCheckAnswerBinding;
import online.javalab.poly.dto.MyFirebaseMessagingService;
import online.javalab.poly.interfaces.SocketAnnotate;
import online.javalab.poly.model.QA;
import online.javalab.poly.model.lesson.Chat;
import online.javalab.poly.model.lesson.Question;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.utils.DialogUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;


/**
 * Created by CanhNamDinh
 * on 05,December,2021
 */
public class CheckAnswerBottomSheetFragment extends BottomSheetDialogFragment implements ChatAdapter.ClickItemChat {

    private BottomSheetCheckAnswerBinding binding;
    private Socket mSocket;
    private Question question;
    private OnBottomSheetListener onBottomSheetListener;
    private List<Chat> mList;
    private ChatAdapter adapter;
    private SharedTopicQuizViewModel viewModel;
    private String title;
    private String QIZ;
    BottomSheetBehavior behavior;


    public interface OnBottomSheetListener {
        void onListener();
    }

    public static CheckAnswerBottomSheetFragment newInstance(Question question, OnBottomSheetListener onBottomSheetListener) {
        CheckAnswerBottomSheetFragment sheetFragment = new CheckAnswerBottomSheetFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Define.BundleKeys.KEY_DATA, question);
        sheetFragment.setArguments(bundle);
        sheetFragment.onBottomSheetListener = onBottomSheetListener;
        return sheetFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            question = (Question) getArguments().get(Define.BundleKeys.KEY_DATA);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.bottom_sheet_check_answer, null, false);
        bottomDialog.setContentView(binding.getRoot());
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
        mSocket();
        behavior = BottomSheetBehavior.from((View) view.getParent());
        initRecyclerView();
        setupState();
        initListener();

        view.setOnTouchListener((v, event) -> {

            hideSoftKeyboard(v);
            return false;
        });

        return bottomDialog;
    }


    private void setupState() {
        binding.setValue(question.getTextAnswer());
        if (question.isCorrected()) {
            binding.bottomSheetContainer.setBackgroundResource(R.drawable.custom_background_bottom_sheet_answer_corrected);
            binding.tvBtsAnswerResultTitle.setText(Define.Answers.ANS_CORRECTED);
            binding.tvBtsAnswerResultTitle.setTextColor(getResources().getColor(R.color.m_green_100));
        } else {
            binding.bottomSheetContainer.setBackgroundResource(R.drawable.custom_background_bottom_sheet_answer_incorrected);
            binding.tvBtsAnswerResultTitle.setText(Define.Answers.ANS_INCORRECT);
            binding.tvBtsAnswerResultTitle.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void initRecyclerView() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mList = new ArrayList<>();
        mList.clear();
        adapter = new ChatAdapter();
        adapter.sortDESC();
        adapter.setListData(mList);
        adapter.notifyDataSetChanged();
        binding.rcvSumDiscuss.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.rcvSumDiscuss.setAdapter(adapter);
        binding.rcvSumDiscuss.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        adapter.setClickItemChat(this);


        binding.icSendQa.setOnClickListener(v -> {
            QA qa = new QA();
            qa.setIdQuestionId(viewModel.getQuestion().getValue().getId() == null ? "" : String.valueOf(viewModel.getQuestion().getValue().getId()));
            qa.setStatus(false);
            qa.setType(true);
            qa.setToken(MyFirebaseMessagingService.getToken(getContext()));
            qa.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
            qa.setUser(GoogleSignIn.getLastSignedInAccount(getContext()).getEmail());
            qa.setTitle(viewModel.getLessonData().getValue().getTitle() + "\n Question : " + viewModel.getCurrentQuestionPosition().getValue() + 1 + "\n" + viewModel.getQuestion().getValue().getQuestion() + "\n" + viewModel.getQuiz().getValue().getName());
            startActivity(new Intent(getActivity(), SendFAQActivity.class).putExtra(Define.BundleKeys.KEY_IS_QA, qa));
        });

        viewModel.getQuestion().observe(this, question -> {
            Chat messageChat = new Chat();
            messageChat.setQuestionId(question.getId());
            mSocket.emit(SocketAnnotate.JoinRoomChat, new Gson().toJson(messageChat));
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        binding.btnBtsNext.setOnClickListener(v -> {
            onBottomSheetListener.onListener();
            dismiss();
        });

        binding.icBtsDiscuss.setOnClickListener(v -> {
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), Define.Message.ASK_TO_CONNECT);
                return;
            }
            viewModel.getHistoryChatListData().observe(this, this::handleListResponse);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            binding.vgChat.setVisibility(View.VISIBLE);
        });

        binding.icBtsDiscussDismiss.setOnClickListener(v -> {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        });
        binding.icChatSend.setOnClickListener(v -> {
            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), Define.Message.ASK_TO_CONNECT);
                return;
            }
            setOnclickSend();
            if (mList != null && !mList.isEmpty()) {
                binding.rcvSumDiscuss.scrollToPosition((mList.size() - 1));
            }

        });

        binding.edtChatBox.setOnTouchListener((v, event) -> {
            if (mList != null && !mList.isEmpty()) {
                binding.rcvSumDiscuss.scrollToPosition((mList.size() - 1));
            }
            return false;
        });


        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint({"SwitchIntDef", "ClickableViewAccessibility"})
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                bottomSheet.setOnTouchListener((v, event) -> {
                    hideSoftKeyboard(bottomSheet);
                    return false;
                });
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        binding.vgChat.setVisibility(View.VISIBLE);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        binding.vgChat.setVisibility(View.GONE);
                        hideSoftKeyboard(bottomSheet);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    public void setOnclickSend() {
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity());

        if (!binding.edtChatBox.getText().toString().isEmpty() && !binding.edtChatBox.getText().toString().equals("")) {
            viewModel.getQuestion().observe(requireParentFragment().getViewLifecycleOwner(), question -> {
                Chat messageChat = new Chat();
                messageChat.setVote(0);
                messageChat.setQuizId(question.getQuizId());
                messageChat.setMessage(binding.edtChatBox.getText().toString().trim());
                messageChat.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
                messageChat.setUsername(signInAccount.getDisplayName());
                messageChat.setQuestionId(question.getId());
                messageChat.setImageUrl(signInAccount.getPhotoUrl().toString());
                mSocket.emit(SocketAnnotate.JoinRoomChat, new Gson().toJson(messageChat));
                binding.edtChatBox.setText("");
            });
        }
    }

    private Socket mSocket() {
        try {
            mSocket = IO.socket(SocketAnnotate.URI_SOCKET);
        } catch (URISyntaxException e) {

        }
        mSocket.connect();
        mSocket.on(SocketAnnotate.ChatAtRoom, userSendPrivate);
        mSocket.on(SocketAnnotate.LoadAgain, LoadAgain);
        return mSocket;
    }


    private Emitter.Listener userSendPrivate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String message = data.optString("data");
                        Chat chat = new Gson().fromJson(message, Chat.class);
                        if (chat != null) {
                            mList.add(chat);
                            adapter.setListData(mList);
                            adapter.notifyDataSetChanged();
                            if (mList != null && !mList.isEmpty()) {
                                binding.rcvSumDiscuss.scrollToPosition((mList.size() - 1));
                            }
                        }

                    }
                });
            }

        }
    };

    private Emitter.Listener LoadAgain = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String message = data.optString("data");
                        List<Chat> list = new Gson().fromJson(message, new TypeToken<List<Chat>>() {
                        }.getType());
                        if (!list.isEmpty()) {
                            mList.clear();
                            mList.addAll(list);
                            adapter.setListData(mList);
                            adapter.notifyDataSetChanged();
                            if (mList != null && !mList.isEmpty()) {
                                binding.rcvSumDiscuss.scrollToPosition((mList.size() - 1));

                            }

                        }
                    }

                });
            }

        }
    };


    private void handleListResponse(ListResponse<Chat> response) {
        switch (response.getType()) {
            case Define.ResponseStatus.LOADING:
                DialogUtil.getInstance(getContext()).show();
                break;
            case Define.ResponseStatus.SUCCESS:
                if (response.getData() != null) {
                    mList.clear();
                    mList.addAll(response.getData());
                    adapter.setListData(mList);
                    adapter.notifyDataSetChanged();
                }
                DialogUtil.getInstance(getContext()).hidden();
                break;
            case Define.ResponseStatus.ERROR:
                DialogUtil.getInstance(getContext()).hidden();
        }
    }


    @Override
    public void clickLike(Chat chat) {
        adapter.sortDESC();
        mSocket.emit(SocketAnnotate.ClickLike, new Gson().toJson(chat), DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
    }


    private void hideSoftKeyboard(View view) {
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}