package dhu.cst.zjm.encryptmvp.api.repository;


import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseRepository {
    Observable<User> loginInternet(User user);
}
