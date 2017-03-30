package dhu.cst.zjm.encryptmvp.api;


import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface ApiService {
    @POST("login.action")
    Observable<User> login(@Body User user);

    @POST("register.action")
    Observable<User> register(@Body User user);

    @FormUrlEncoded
    @POST("getFileList.action")
    Observable<List<File>> getFileList(@Field("owner") String id);

    @FormUrlEncoded
    @POST("getFileListByPaper.action")
    Observable<List<File>> getFileListByPaper(@FieldMap Map<String, String> params);

    @POST("getEncryptType.action")
    Observable<List<EncryptType>> getEncryptType();

    @FormUrlEncoded
    @POST("encryptFile.action")
    Observable<EncryptRelation> encryptFile(@FieldMap Map<String, String> params);


    @Multipart
    @POST("uploadFile.action")
    Observable<File> uploadFile(@PartMap Map<String,RequestBody> map);

    @FormUrlEncoded
    @Streaming
    @POST("downloadFile.action")
    Observable<ResponseBody> downloadFile(@Field("id") String downloadId);
}
