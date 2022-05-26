package online.javalab.poly.dto.network;

import com.google.gson.annotations.SerializedName;

import online.javalab.poly.utils.Define;

public class RequestError {
    @SerializedName(Define.Api.BaseResponse.ERROR_CODE)
    private String errorCode;

    @SerializedName(Define.Api.BaseResponse.ERROR_MESSAGE)
    private String errorMessage;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
