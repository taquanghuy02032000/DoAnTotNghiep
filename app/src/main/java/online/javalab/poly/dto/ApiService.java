package online.javalab.poly.dto;

import io.reactivex.rxjava3.core.Single;
import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.base.base.ObjectResponse;
import online.javalab.poly.model.Profile;

import online.javalab.poly.model.TopLesson;
import online.javalab.poly.model.QA;
import online.javalab.poly.model.SendMail;
import online.javalab.poly.model.User;
import online.javalab.poly.model.lesson.Chat;
import online.javalab.poly.model.lesson.LearnProgress;
import online.javalab.poly.model.lesson.Lesson;

import online.javalab.poly.model.lesson.ProgressRequest;
import retrofit2.Call;

import online.javalab.poly.model.rank.Datum;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/get-score-profile")
    Single<ListResponse<Profile>> getProfileScore(@Query("userId") String userId);

    @GET("api/get-all-in-lesson")
    Single<ListResponse<Lesson>> getAllLessons();

    @GET("api/get-all-process")
    Single<ListResponse<LearnProgress>> getAllProgress(@Query("userId") String userId);

    @GET("api/get-all-process")
    Single<ObjectResponse<LearnProgress>> getProgress(@Query("userId") String userId, @Query("lessonId") String lessonId);

    @POST("api/update-process")
    Single<ObjectResponse<LearnProgress>> createOrUpdateProgress(@Body() ProgressRequest body);

    @POST("api/add-qa")
    Single<ObjectResponse<QA>> createSendFAQ(@Body() QA body);

    @POST("api/send-notifi-with-user")
    Single<ObjectResponse<QA>> creatNotifi(@Body() QA body);

    @GET("api/get-comment-by-question")
    Single<ListResponse<Chat>> getCommentId(@Query("questionId") String questionId);

    @FormUrlEncoded
    @POST("api/update-comment")
    Single<ListResponse<Chat>> likeComentId(@Field("id") String id,
                                            @Field("userId") String userId);

    @POST("api/insert-user")
    Single<ObjectResponse<User>> getOrCreateNewUser(@Body() User body);

    @GET("api/get-user")
    Single<ObjectResponse<User>> getUserInfo(@Query("gmail") String gmail);

    @POST("api/update-mark-user")
    Single<ObjectResponse<User>> updateUserMark(@Body float mark);

    @GET("api/get-top-user?topUser=100")
    Single<ListResponse<Datum>> getProfileRank();


    @GET("api/top-lesson-score")
    Single<ListResponse<TopLesson>> getTopLesson(@Query("userId") String userId);
    @GET("api/sendmail_user")
    Single<ObjectResponse<SendMail>> getSendMail(@Query("email") String gmail, @Query("subject") String subject);
}
