package me.citrafa.asistenkuliahku;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by SENSODYNE on 01/05/2017.
 */

public interface ClientServiceAPI {
    @POST("upload/all")
    Call<ResponseBody> SyncAll();
    @GET("upload/all")
    Call<ResponseBody> getAll();
    @GET("upload/all")
    Call<ResponseBody> responseMessage();
}
