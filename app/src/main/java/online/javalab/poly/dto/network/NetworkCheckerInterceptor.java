package online.javalab.poly.dto.network;

import android.content.Context;



import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import online.javalab.poly.exception.NoInternetConnectionException;
import online.javalab.poly.utils.CommonExt;
import online.javalab.poly.utils.DeviceUtil;

public class NetworkCheckerInterceptor implements Interceptor {

    private Context context;

    public NetworkCheckerInterceptor(Context context) {
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (DeviceUtil.hasConnection(context)) {
            return chain.proceed(chain.request());
        } else {
            throw new NoConnectivityException();
        }
    }

    public static class NoConnectivityException extends IOException {
        @Override
        public String getMessage() {
            return "No internet connection";
        }
    }
}
