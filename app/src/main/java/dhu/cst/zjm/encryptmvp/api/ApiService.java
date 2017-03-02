package dhu.cst.zjm.encryptmvp.api;


import java.util.List;

import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
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
    Observable<List<ServerFile>> getMenuFileList(@Field("get_menu_file_list_key") String id);
}
