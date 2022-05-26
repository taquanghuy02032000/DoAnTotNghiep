package online.javalab.poly.fragments.lesson;

import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import online.javalab.poly.R;
import online.javalab.poly.activitys.lesson.TopicsContainerActivity;
import online.javalab.poly.adapters.lesson.AnswerAdapter;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.base.base.RecyclerViewAdapter;
import online.javalab.poly.databinding.FragmentQuizBinding;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.model.lesson.Question;
import online.javalab.poly.model.lesson.Quiz;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Define;
import online.javalab.poly.utils.DeviceUtil;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;


public class QuizFragment extends BaseFragment<SharedTopicQuizViewModel, FragmentQuizBinding> {
    private int answer = 0;
    private List<Question> questionList = null;
    private Lesson lesson = null;
    private Quiz quiz = null;
    private AnswerAdapter adapter;
    private int questionPosition = 0;
    private Question question = null;
    private ProgressRequest progressRequest;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_quiz;
    }

    @Override
    public void backFromAddFragment() {

    }

    @Override
    public boolean backPressed() {
        askToBackDialog();
        return false;
    }

    @Override
    public void initView() {
        binding.setLifecycleOwner(this);
        adapter = new AnswerAdapter(requireActivity(), true);
        initRecyclerView();
        progressRequest = new ProgressRequest();
        progressRequest.setUserId(DataStorageManager.getStringValue(Define.StorageKey.USER_ID));
    }

    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false);
        binding.rcvAnswer.setLayoutManager(layoutManager);
    }

    @Override
    public void initData() {
        viewModel.getQuizStatus().setValue(0);
        lesson = viewModel.getLessonData().getValue();
        if (lesson == null) return;
        progressRequest.setLessonId(lesson.getId());
        if (lesson.getQuiz() != null) {
            quiz = lesson.getQuiz();
            questionList = quiz.getQuestions();
            ((TopicsContainerActivity) requireActivity()).setActionBarTitle(quiz.getName() != null ? quiz.getName() : "");

        }
        viewModel.getCurrentQuestionPosition().setValue(questionPosition);//0

        viewModel.getCurrentQuestionPosition().observe(getViewLifecycleOwner(), position -> {
            viewModel.setProgressQuiz(position);
            viewModel.nextQuestion(position);
        });

        viewModel.getQuestion().observe(getViewLifecycleOwner(), question -> {
            if (question != null) {
                this.question = question;
                binding.setPosition(questionPosition + 1);
                binding.setQuestion(question);
                adapter.refresh(question.getAnswers());
                binding.rcvAnswer.setAdapter(adapter);
            }
        });

        viewModel.getQuizStatus().observe(getViewLifecycleOwner(), status -> {
            if (status == Define.QuizStatus.STATUS_IS_DONE) {
                if (DeviceUtil.hasConnection(requireActivity())) {
                    progressRequest.setQuizStatus(status);
                    viewModel.fetchCreateOrUpdateProgress(progressRequest);
                }

                if (mViewController != null){
                    mViewController.addFragment(QuizSummaryFragment.class, null);}
            }
        });

        viewModel.getProgressBar().observe(getViewLifecycleOwner(), progress -> {
            binding.progressQuizCompleteState.setProgress(progress);
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initListeners() {
        binding.icExitQuiz.setOnClickListener(v -> {
            askToBackDialog();
        });

        binding.btnQuizCheckAnswer.setOnClickListener(v -> {

            Supplier<Stream<Object>> streamSupplier = () ->
                    adapter.getListWrapperModels().stream().filter(RecyclerViewAdapter.ModelWrapper::isSelected).map(
                            RecyclerViewAdapter.ModelWrapper::getModel
                    );

            if (!streamSupplier.get().findFirst().isPresent()) {
                Toast.makeText(requireActivity(), "Please choose your answer", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!DeviceUtil.hasConnection(requireActivity())) {
                CommonExt.toast(requireActivity(), Define.Message.ASK_TO_CONNECT);
                return;
            }

            String value = streamSupplier.get().findFirst().get().toString().trim();
            question.setCorrected(question.checkAnswer(value));
            if (question.isCorrected()) {
                viewModel.setCorrectedIncrement();
            }
            CheckAnswerBottomSheetFragment bottomSheetFragment = CheckAnswerBottomSheetFragment.newInstance(question, bottomSheetListener());
            bottomSheetFragment.show(getChildFragmentManager(), bottomSheetFragment.getTag());
            bottomSheetFragment.setCancelable(false);

        });

    }

    private CheckAnswerBottomSheetFragment.OnBottomSheetListener bottomSheetListener() {
        return () -> {
            questionPosition++;
            viewModel.getCurrentQuestionPosition().setValue(questionPosition);
        };
    }

    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
    }

    private void askToBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage("Do you want to quit?");
        builder.setCancelable(true);
        builder.setPositiveButton("OK", (dialog, which) -> {
            assert mViewController != null;
            mViewController.backFromAddFragment(null);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}