package dhu.cst.zjm.encryptmvp.api;


import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.util.UploadFileRequestBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface ApiService {
    @POST("login_internet")
    Observable<User> Login_Internet(@Body User user);

    @POST("register_try")
    Observable<User> registerTry(@Body User user);

    @FormUrlEncoded
    @POST("get_menu_file_list")
    Observable<List<ServerFile>> getFileList(@Field("get_menu_file_list_key") String id);

    @POST("get_menu_file_type")
    Observable<List<EncryptType>> getEncryptType();

    @Multipart
    @POST("upload_file")
    Observable<String> uploadFile(@Header("upload_file_id_key") String id,
                                  @PartMap Map<String, UploadFileRequestBody> fileMap);
}
