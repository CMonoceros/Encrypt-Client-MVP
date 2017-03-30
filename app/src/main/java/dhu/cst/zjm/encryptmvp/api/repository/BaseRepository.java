package dhu.cst.zjm.encryptmvp.api.repository;


import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by zjm on 2017/2/23.
 */

public interface BaseRepository {
    Observable<User> login(User user);

    Observable<User> register(User user);

    Observable<List<File>> getFileList(String id);

    Observable<List<File>> getFileListByPaper(Map<String, String> params);

    Observable<List<EncryptType>> getEncryptType();

    Observable<EncryptRelation> encryptFile(Map<String, String> params);

    Observable<File> uploadFile(Map<String,RequestBody> map);

    Observable<ResponseBody> downloadFile(String downloadId);
}
