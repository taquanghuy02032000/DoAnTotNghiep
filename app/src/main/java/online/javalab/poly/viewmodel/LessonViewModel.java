package online.javalab.poly.viewmodel;

import androidx.core.util.Pair;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.BiFunction;
import online.javalab.poly.base.BaseViewModel;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.model.lesson.BaseLessonProgress;
import online.javalab.poly.model.lesson.FilterTabLesson;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.storage.DataStorageManager;
import online.javalab.poly.utils.Define;


public class LessonViewModel extends BaseViewModel {
    private final MutableLiveData<List<Lesson>> lessonListData = new MutableLiveData<>();
    private final MutableLiveData<Lesson> selectedLesson = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<Lesson>> lessonListResponse = new MutableLiveData<>();
    private final MutableLiveData<ListResponse<LearnProgress>> progressListResponse = new MutableLiveData<>();
    private final MutableLiveData<ObjectResponse<LearnProgress>> progressResponse = new MutableLiveData<>();
    private final MutableLiveData<BaseLessonProgress> baseLessonProgress = new MutableLiveData<>();
    private final MutableLiveData<List<Lesson>> listLessonFilter = new MutableLiveData<>();

    public MutableLiveData<Lesson> getSelectedLesson() {
        return selectedLesson;
    }

    public MutableLiveData<ListResponse<Lesson>> getLessonListResponse() {
        return lessonListResponse;
    }

    public MutableLiveData<List<Lesson>> getLessonListData() {
        return lessonListData;
    }

    public MutableLiveData<ListResponse<LearnProgress>> getProgressListResponse() {
        return progressListResponse;
    }

    public MutableLiveData<BaseLessonProgress> getBaseLessonProgress() {
        return baseLessonProgress;
    }

    public MutableLiveData<ObjectResponse<LearnProgress>> getProgressResponse() {
        return progressResponse;
    }

    public MutableLiveData<List<Lesson>> getListLessonFilter() {
        return listLessonFilter;
    }

    public void fetchLessonList(boolean isShowLoading) {
        mDisposable.add(
                repository.getLessons()
                        .zipWith(repository.getAllProgress("" + DataStorageManager.getStringValue(Define.StorageKey.USER_ID)),
                                (BiFunction<ListResponse<Lesson>, ListResponse<LearnProgress>, Pair>) Pair::new
                        )
                        .doOnSubscribe(disposable -> {
                            if (isShowLoading) {
                                lessonListResponse.setValue(new ListResponse<Lesson>().loading());
                            }
                        })
                        .subscribe(this::onNext, this::onError)
        );

    }

    public void fetchProgressList(boolean isShowLoading) {
        mDisposable.add(
                repository.getAllProgress("" + DataStorageManager.getStringValue(Define.StorageKey.USER_ID))
                        .doOnSubscribe(disposable -> {
                            if (isShowLoading) {
                                progressListResponse.setValue(new ListResponse<LearnProgress>().loading());
                            }
                        })
                        .subscribe(this::onProgressSuccess, throwable -> progressListResponse.setValue(new ListResponse<LearnProgress>().error(throwable)))
        );
    }

    public void fetchCreateOrUpdateProgress(ProgressRequest request) {
        mDisposable.add(
                repository.createOrUpdateProgress(request)
                        .doOnSubscribe(disposable -> progressResponse.setValue(new ObjectResponse<LearnProgress>().loading()))
                        .subscribe((response) -> {
                            if (response.getData() != null) {
                                progressResponse.setValue(new ObjectResponse<LearnProgress>().success(response.getData()));
                            }

                        }, (throwable) -> {
                            progressResponse.setValue(new ObjectResponse<LearnProgress>().error(throwable));
                        })
        );
    }

    private void onError(Throwable throwable) {
        lessonListResponse.setValue(new ListResponse<Lesson>().error(throwable));
        progressListResponse.setValue(new ListResponse<LearnProgress>().error(throwable));
    }

    private void onNext(Pair<ListResponse<Lesson>, ListResponse<LearnProgress>> pair) {
        ListResponse<Lesson> mLessonListResponse = pair.first;
        ListResponse<LearnProgress> learnProgressListResponse = pair.second;

        if (mLessonListResponse != null) {
            if (mLessonListResponse.getData() != null)
                lessonListResponse.setValue(new ListResponse<Lesson>().success(mLessonListResponse.getData()));
        }

        if (learnProgressListResponse != null) {
            if (learnProgressListResponse.getData() != null)
                progressListResponse.setValue(new ListResponse<LearnProgress>().success(learnProgressListResponse.getData()));
        }
    }

    private void onProgressSuccess(ListResponse<LearnProgress> progressListResponse) {
        if (progressListResponse.getData() != null)
            getProgressListResponse().setValue(new ListResponse<LearnProgress>().success(progressListResponse.getData()));
    }

    public void handleProgressResponse(List<LearnProgress> progressList) {
        List<Lesson> lessonList = lessonListData.getValue();
        if (lessonList != null && progressList != null) {
            for (LearnProgress progress : progressList) {
                for (Lesson lesson : lessonList) {
                    if (progress.getLessonId().equals(lesson.getId())) {
                        lesson.setProgress(progress);
                    }
                }
            }
            lessonListData.setValue(lessonList);
        }
    }


    public void setLessonListData(Lesson oldLesson, Lesson updatedLesson) {
        if (oldLesson == null || updatedLesson == null) return;
        List<Lesson> lessonList = lessonListData.getValue();
        if (lessonList != null) {
            if (lessonList.contains(oldLesson)) {
                int position = lessonList.indexOf(oldLesson);
                lessonList.set(position, updatedLesson);
                getLessonListData().setValue(lessonList);
            }
        }
    }

    public void setOnFilterLesson(int tapId) {
        if (lessonListData.getValue() == null) return;
        List<Lesson> listRoot = lessonListData.getValue();
        List<Lesson> listFilter = new ArrayList<>();
        if (tapId == Define.Tab.TAB_ID_ALL) {
            listFilter.addAll(listRoot);

        } else if (tapId == Define.Tab.TAB_ID_NOT_START) {
            for (Lesson lesson : listRoot) {
                if (lesson.getProgress() == null || lesson.getProgress().getStatus() == Define.Status.STATUS_NOT_STARTED) {
                    listFilter.add(lesson);
                }
            }
        } else if (tapId == Define.Tab.TAB_ID_LEARNING) {
            for (Lesson lesson : listRoot) {
                if (lesson.getProgress() != null) {
                    int tStt = lesson.getProgress().getStatus();
                    int qStt = lesson.getProgress().getQuizStatus();
                    if (tStt != Define.Status.STATUS_NOT_STARTED && qStt != Define.QuizStatus.STATUS_IS_DONE) {
                        listFilter.add(0,lesson);
                    }
                }
            }

        } else if (tapId == Define.Tab.TAB_ID_DONE) {
            for (Lesson lesson : listRoot) {
                if (lesson.getProgress() != null) {
                    int tStt = lesson.getProgress().getStatus();
                    int qStt = lesson.getProgress().getQuizStatus();
                    if (tStt == Define.Status.STATUS_COMPLETED && qStt == Define.QuizStatus.STATUS_IS_DONE) {
                        listFilter.add(lesson);
                    }
                }
            }
        }
        //
        listLessonFilter.setValue(listFilter);
    }

}