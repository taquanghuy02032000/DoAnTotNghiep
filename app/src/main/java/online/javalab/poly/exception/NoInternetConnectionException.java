package online.javalab.poly.exception;

import androidx.annotation.Nullable;

import java.io.IOException;

public class NoInternetConnectionException extends IOException {

    @Nullable
    @Override
    public String getMessage() {
        return "No internet connection";
    }
}
