package online.javalab.poly;

import online.javalab.poly.model.ServerRespone;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SendFAQApi {
    SendFAQApi sendFaqApi = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.1.5:3000//api/")
            .build()
            .create(SendFAQApi.class);


    @FormUrlEncoded
    @POST("add-qa")
    Call<ServerRespone> createSendFAQ(@Field("userId") String gmail,
                                      @Field("title") String title,
                                      @Field("content") String content);
}
