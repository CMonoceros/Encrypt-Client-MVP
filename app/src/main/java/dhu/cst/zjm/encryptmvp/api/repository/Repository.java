package dhu.cst.zjm.encryptmvp.api.repository;

import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.api.ApiService;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.util.UploadFileRequestBody;
import okhttp3.RequestBody;
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
    public Observable<List<ServerFile>> getFileList(String id) {
        return mApiService.getFileList(id);
    }

    @Override
    public Observable<List<EncryptType>> getEncryptType() {
        return mApiService.getEncryptType();
    }

    @Override
    public Observable<String> uploadFile(String id, Map<String, UploadFileRequestBody> fileMap) {
        return mApiService.uploadFile(id, fileMap);
    }


}
