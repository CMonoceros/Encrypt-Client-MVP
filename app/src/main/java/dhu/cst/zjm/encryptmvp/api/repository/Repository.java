package dhu.cst.zjm.encryptmvp.api.repository;

import dhu.cst.zjm.encryptmvp.api.ApiService;
import retrofit2.Retrofit;

/**
 * Created by zjm on 2017/2/23.
 */

public class Repository implements BaseRepository {

    private ApiService mApiService;

    public Repository(Retrofit retrofit) {
        mApiService = retrofit.create(ApiService.class);
    }
}
