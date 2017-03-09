package dhu.cst.zjm.encryptmvp.api.repository;


import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.util.UploadFileRequestBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseRepository {
    Observable<User> loginInternet(User user);

    Observable<User> registerTry(User user);

    Observable<List<ServerFile>> getFileList(String id);

    Observable<List<EncryptType>> getEncryptType();

    Observable<String> uploadFile(String id,Map<String, UploadFileRequestBody> fileMap);
}
