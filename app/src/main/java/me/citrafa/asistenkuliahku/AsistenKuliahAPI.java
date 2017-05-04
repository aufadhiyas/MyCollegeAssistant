package me.citrafa.asistenkuliahku;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by SENSODYNE on 01/05/2017.
 */

public interface AsistenKuliahAPI {
    @POST("upload/all")
    Call<ResponseBody> KirimSemua();

}
