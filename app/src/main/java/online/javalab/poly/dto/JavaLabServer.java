package online.javalab.poly.dto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import online.javalab.poly.Application;
import online.javalab.poly.dto.network.NetworkCheckerInterceptor;
import online.javalab.poly.interfaces.JavaLabApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class JavaLabServer {
    public static Retrofit retrofit;

    public static final String BASE_URL_JAVA_LAB = "https://it1utc.herokuapp.com/";

    public static Retrofit getJavaLab(){
        if (retrofit == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = builder
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new NetworkCheckerInterceptor(Application.getInstance()))
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_JAVA_LAB)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService(){
        return getJavaLab().create(ApiService.class);
    }

    public static JavaLabApi getApiInterface(){
        return getJavaLab().create(JavaLabApi.class);
    }
}
