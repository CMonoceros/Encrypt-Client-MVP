package dhu.cst.zjm.encryptmvp.api.repository;


import java.util.List;

import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseRepository {
    Observable<User> loginInternet(User user);

    Observable<User> registerTry(User user);

    Observable<List<ServerFile>> getMenuFileList(String id);
}
