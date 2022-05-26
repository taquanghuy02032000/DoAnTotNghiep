package online.javalab.poly.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import online.javalab.poly.base.BaseViewModel;

import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.model.QA;
import online.javalab.poly.model.SendMail;
import online.javalab.poly.model.lesson.LearnProgress;

import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.model.lesson.Chat;

import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.model.lesson.Question;
import online.javalab.poly.model.lesson.Quiz;
import online.javalab.poly.model.lesson.Topic;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.Constants;

/**
 * Todo by CanhNamDinh on: 1/10/2022
 */
public class SharedTopicQuizViewModel extends BaseViewModel {

    private final MutableLiveData<Lesson> lessonData = new MutableLiveData<>();
    private final MutableLiveData<Topic> topicData = new MutableLiveData<>();
    private final MutableLiveData<List<Topic>> topicListData = new MutableLiveData<>();
    private final MutableLiveData<LearnProgress> progressData = new MutableLiveData<>();
    private final MutableLiveData<LearnProgress> progressUpdateData = new MutableLiveData<>();
    private final MutableLiveData<ObjectResponse<LearnProgress>> progressResponse = new MutableLiveData<>();
    private final MutableLiveData<Float> mark = new MutableLiveData<>();
    private final MutableLiveData<Integer> correctedCount = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentQuestionPosition = new MutableLiveData<>();
    private final MutableLiveData<List<Question>> questionList = new MutableLiveData<>();
    private final MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<Chat>> historyChat = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<Chat>> likeChat = new MutableLiveData<>();
    private final MutableLiveData<Quiz> quiz = new MutableLiveData<>();
    private final MutableLiveData<Integer> quizStatus = new MutableLiveData<>();
    private final MutableLiveData<Integer> quizProgress = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLastTopic = new MutableLiveData<>();
    private final MutableLiveData<ProgressRequest> progressRequest = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isUpdate = new MutableLiveData<>();

    private final MutableLiveData<ObjectResponse<SendMail>> mMail = new MutableLiveData<>();
    private final MutableLiveData<ObjectResponse<QA>> mFeedBack = new MutableLiveData<>();
    private final MutableLiveData<ObjectResponse<QA>> mNotifi = new MutableLiveData<>();

    public MutableLiveData<ObjectResponse<SendMail>> getSendMail() {
        return mMail;
    }

    public MutableLiveData<ObjectResponse<QA>> getmFeedBack() {
        return mFeedBack;
    }

    public MutableLiveData<Boolean> isUpdate() {
        return isUpdate;
    }

    public MutableLiveData<ProgressRequest> getProgressRequest() {
        return progressRequest;
    }

    public MutableLiveData<ListResponse<Chat>> getHistoryChatListData() {
        if (currentQuestion.getValue() != null) {
            loadChatHistoryList(currentQuestion.getValue().getId());
        }
        return historyChat;
    }

    public MutableLiveData<ListResponse<Chat>> getHistoryv2(String id, String userId) {
        LikeChat(id, userId);
        return historyChat;
    }

    public MutableLiveData<Lesson> getLessonData() {
        return lessonData;
    }

    public MutableLiveData<LearnProgress> getProgressData() {
        return progressData;
    }

    public MutableLiveData<LearnProgress> getProgressUpdateData() {
        return progressUpdateData;
    }

    public MutableLiveData<ObjectResponse<LearnProgress>> getProgressResponse() {
        return progressResponse;
    }

    public MutableLiveData<Topic> getTopicData() {
        return topicData;
    }

    public MutableLiveData<List<Topic>> getTopicListData() {
        return topicListData;
    }

    public MutableLiveData<Float> getMark() {
        return mark;
    }

    public MutableLiveData<Integer> getCorrectedCount() {
        return correctedCount;
    }

    public MutableLiveData<Integer> getCurrentQuestionPosition() {
        return currentQuestionPosition;
    }

    public MutableLiveData<List<Question>> getQuestionList() {
        return questionList;
    }

    public MutableLiveData<Question> getQuestion() {
        return currentQuestion;
    }

    public MutableLiveData<Quiz> getQuiz() {
        return quiz;
    }

    public MutableLiveData<Integer> getQuizStatus() {
        return quizStatus;
    }

    public MutableLiveData<Integer> getProgressBar() {
        return quizProgress;
    }

    public MutableLiveData<Boolean> isLastTopic() {
        return isLastTopic;
    }

    public void setMark(int totalQuestion, int totalCorrected) {
        float aMark = 100f / (float) totalQuestion;
        float finalMark = totalCorrected * aMark;
        getMark().setValue(finalMark);
    }

    public void setCorrectedIncrement() {
        if (getCorrectedCount().getValue() != null) {
            int mMark = getCorrectedCount().getValue() + 1;
            getCorrectedCount().setValue(mMark);
        } else {
            getCorrectedCount().setValue(1);
        }
    }

    public void nextQuestion(int currentPosition) {
        if (currentPosition < questionList.getValue().size()) {
            currentQuestion.setValue(questionList.getValue().get(currentPosition));
        } else {
            quizStatus.setValue(1);
        }
    }

    public void setProgressQuiz(int position) {
        if (position == 0) {
            quizProgress.setValue(0);
        } else {
            int length = getQuestionList().getValue().size();
            if (length == 0) return;
            int progress = CommonExt.progressCalculator(position, length);
            quizProgress.setValue(progress);
        }
    }


    public void fetchCreateOrUpdateProgress(ProgressRequest request) {
        mDisposable.add(
                repository.createOrUpdateProgress(request)
                        .doOnSubscribe(disposable -> progressResponse.setValue(new ObjectResponse<LearnProgress>().loading()))
                        .subscribe((response) -> {
                                    if (response.getData() != null) {
                                        progressResponse.setValue(new ObjectResponse<LearnProgress>().success(response.getData()));
                                    }
                                },
                                (throwable) -> progressResponse.setValue(new ObjectResponse<LearnProgress>().error(throwable))
                        )
        );
    }

    public void checkTopicIndex(Topic currentTopic) {
        if (lessonData.getValue() == null) return;
        List<Topic> topicList = lessonData.getValue().getTopics();
        if (topicList == null || topicList.size() == 0) return;
        int currentTopicPosition = topicList.indexOf(currentTopic);
        if (currentTopicPosition == topicList.size() - 1) {
            isLastTopic.setValue(true);
        } else {
            isLastTopic.setValue(false);
        }
    }

    public void nextTopic(Topic currentTopic) {
        if (lessonData.getValue() == null) return;
        List<Topic> topicList = lessonData.getValue().getTopics();
        if (topicList == null || topicList.size() == 0) return;
        int currentTopicPosition = topicList.indexOf(currentTopic);
        if (topicList.get(currentTopicPosition + 1) != null) {
            topicData.setValue(topicList.get(currentTopicPosition + 1));
        }
    }

    public void setTopicListItemComplete(Topic topic) {
        List<Topic> topicList = topicListData.getValue();
        if (topicList != null) {
            if (topicList.contains(topic)) {
                int index = topicList.indexOf(topic);
                topic.setCompleted(true);
                topicList.set(index, topic);
                topicListData.setValue(topicList);
            }
        }
    }

    public void loadChatHistoryList(String id) {
        mDisposable.add(
                repository.getCommentId(id)
                        .doOnSubscribe(disposable -> {
                            historyChat.setValue(new ListResponse<Chat>().loading());
                        })
                        .subscribe(this::onNext, this::onError)
        );

    }

    public void LikeChat(String id, String userId) {
        mDisposable.add(
                repository.likeComentId(id, userId)
                        .doOnSubscribe(disposable -> {
                            historyChat.setValue(new ListResponse<Chat>().loading());
                        })
                        .subscribe(this::onNext, this::onError)
        );

    }


    private void onError(Throwable throwable) {
        Log.d(Constants.TAG, "onError: " + throwable.getMessage());
    }

    private void onNext(ListResponse<Chat> chat) {
        if (chat.getData() != null)
            historyChat.setValue(new ListResponse<Chat>().success(chat.getData()));
    }

    //*todo by canhnd
    public void sendMailUser(String mail, String subject) {
        mDisposable.add(repository.getSendMail(mail, subject)
                .doOnSubscribe(disposable -> mMail.setValue(new ObjectResponse<SendMail>().loading()))
                .subscribe((response) -> {
                    if (response.getData() != null) {
                        mMail.setValue(new ObjectResponse<SendMail>().success(response.getData()));
                    }

                }, (throwable) -> {
                    mMail.setValue(new ObjectResponse<SendMail>().error(throwable));
                })
        );

    }

    public void createQA(QA qa) {
        mDisposable.add(repository.createSendFAQ(qa)
                .doOnSubscribe(disposable -> mFeedBack.setValue(new ObjectResponse<QA>().loading()))
                .subscribe((response) -> {
                    if (response.getData() != null) {
                        mFeedBack.setValue(new ObjectResponse<QA>().success(response.getData()));
                    }

                }, (throwable) -> {
                    mFeedBack.setValue(new ObjectResponse<QA>().error(throwable));
                })
        );

    }

    public void creatNotifi(QA qa) {
        mDisposable.add(repository.creatNotifi(qa)
                .doOnSubscribe(disposable -> mNotifi.setValue(new ObjectResponse<QA>().loading()))
                .subscribe((response) -> {
                    if (response.getData() != null) {
                        mNotifi.setValue(new ObjectResponse<QA>().success(response.getData()));
                    }

                }, (throwable) -> {
                    mNotifi.setValue(new ObjectResponse<QA>().error(throwable));
                })
        );

    }


}
