package dhu.cst.zjm.encryptmvp.api.repository;

import dhu.cst.zjm.encryptmvp.api.ApiService;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public class Repository implements BaseRepository {

    private ApiService mApiService;

    public Repository(Retrofit retrofit) {
        mApiService = retrofit.create(ApiService.class);
    }

    @Override
    public Observable<User> loginInternet(User user) {
        return mApiService.Login_Internet(user);
    }
}
