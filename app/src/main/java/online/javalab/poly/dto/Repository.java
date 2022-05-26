package online.javalab.poly.dto;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.model.Profile;
import online.javalab.poly.model.QA;
import online.javalab.poly.model.SendMail;
import online.javalab.poly.model.TopLesson;
import online.javalab.poly.model.User;
import online.javalab.poly.model.lesson.Chat;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;
import online.javalab.poly.model.lesson.ProgressRequest;
import online.javalab.poly.model.rank.Datum;

public class Repository {
    private ApiService apiService;

    public Repository(ApiService apiService) {
        this.apiService = apiService;
    }

    public Single<ListResponse<Lesson>> getLessons() {
        return apiService.getAllLessons()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ListResponse<LearnProgress>> getAllProgress(String userId) {
        return apiService.getAllProgress(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<LearnProgress>> getProgress(String userId, String lessonId) {
        return apiService.getProgress(userId, lessonId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<LearnProgress>> createOrUpdateProgress(ProgressRequest body) {
        return apiService.createOrUpdateProgress(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ListResponse<Chat>> likeComentId(String id, String userId) {
        return apiService.likeComentId(id, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ListResponse<Chat>> getCommentId(String questionId) {
        return apiService.getCommentId(questionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<User>> getOrCreateNewUser(User body) {
        return apiService.getOrCreateNewUser(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<User>> getUserInfo(String gmail) {
        return apiService.getUserInfo(gmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<User>> updateUserMark(float mark) {
        return apiService.updateUserMark(mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ListResponse<Profile>> getProfileScore(String userId) {
        return apiService.getProfileScore(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ListResponse<Datum>> getProfileRank() {
        return apiService.getProfileRank()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Single<ListResponse<TopLesson>> getTopLesson(String userID) {
        return apiService.getTopLesson(userID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<SendMail>> getSendMail(String mail, String subject) {
        return apiService.getSendMail(mail, subject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<QA>> createSendFAQ(QA qa) {

        return apiService.createSendFAQ(qa)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ObjectResponse<QA>> creatNotifi(QA qa) {
        return apiService.creatNotifi(qa)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
