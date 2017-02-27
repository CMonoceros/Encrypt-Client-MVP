package dhu.cst.zjm.encryptmvp.api;


import dhu.cst.zjm.encryptmvp.mvp.model.User;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface ApiService {
    @POST("/login_internet")
    Observable<User> Login_Internet(@Body User user);
}
