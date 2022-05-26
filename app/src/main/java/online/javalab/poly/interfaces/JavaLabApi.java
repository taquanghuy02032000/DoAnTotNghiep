package online.javalab.poly.interfaces;

import java.util.List;

import online.javalab.poly.base.base.ListResponse;
import online.javalab.poly.model.Program;
import online.javalab.poly.model.lesson.Chat;
import online.javalab.poly.model.rank.Ratings;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JavaLabApi {
    @GET("api/get-top-user?topUser=100")
    Call<Ratings> getRatings();

    @GET("api/get-all-in-program")
    Call<List<Program>> getPrograms();

    @FormUrlEncoded
    @POST("api/update-comment")
    Call<ListResponse<Chat>> likeComentId(@Field("id") String id,
                                          @Field("userId") String userId);

}
