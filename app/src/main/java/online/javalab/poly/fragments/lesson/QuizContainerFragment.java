package online.javalab.poly.fragments.lesson;

import android.util.Log;

import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import online.javalab.poly.R;
import online.javalab.poly.base.base.BaseFragment;
import online.javalab.poly.base.base.ViewController;
import online.javalab.poly.databinding.FragmentQuizContainerBinding;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.Question;
import online.javalab.poly.model.lesson.Quiz;
import online.javalab.poly.viewmodel.SharedTopicQuizViewModel;


public class QuizContainerFragment extends BaseFragment<SharedTopicQuizViewModel, FragmentQuizContainerBinding> {
    private List<Question> questionList = null;
    private Quiz quiz = null;
    private Lesson lesson = null;



    @Override
    protected SharedTopicQuizViewModel onCreateViewModel() {
        return new ViewModelProvider(requireActivity()).get(SharedTopicQuizViewModel.class);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_quiz_container;
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
        binding.setLifecycleOwner(this);
        ViewController newViewController = new ViewController(getChildFragmentManager(), R.id.layout_container_quiz);
        setViewController(newViewController);
    }

    @Override
    public void initData() {
        HashMap<String, Object> hashMap = new HashMap<>();
        lesson = viewModel.getLessonData().getValue();
        if (lesson == null) return;

        if (lesson.getQuiz() != null) {
            quiz = lesson.getQuiz();
            questionList = quiz.getQuestions();
            viewModel.getQuiz().setValue(quiz);
            viewModel.getQuestionList().setValue(quiz.getQuestions());
            mViewController.addFragment(QuizFragment.class, hashMap);
        }




    }

    @Override
    public void initListeners() {

    }
}