package dhu.cst.zjm.encryptmvp.api.repository;

import java.util.List;

import dhu.cst.zjm.encryptmvp.api.ApiService;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
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

    @Override
    public Observable<User> registerTry(User user) {
        return mApiService.registerTry(user);
    }

    @Override
    public Observable<List<ServerFile>> getMenuFileList(String id) {
        return mApiService.getMenuFileList(id);
    }


}
