package online.javalab.poly.dto.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import online.javalab.poly.utils.Define;

public class ApiArrayResponse<T> {

    @SerializedName(Define.Api.BaseResponse.SUCCESS)
    private Integer success;

    @SerializedName(Define.Api.BaseResponse.DATA)
    private List<T> data;

    @SerializedName(Define.Api.BaseResponse.PAGE)
    private Integer page;

    @SerializedName(Define.Api.BaseResponse.ERROR)
    private RequestError error;

    public Integer getSuccess() {
        return success;
    }

    public boolean isSuccess() {
        return success == 1;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public RequestError getRequestError() {
        return error;
    }

    public void setRequestError(RequestError error) {
        this.error = error;
    }
}
