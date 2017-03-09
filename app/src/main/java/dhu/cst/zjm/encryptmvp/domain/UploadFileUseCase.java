package dhu.cst.zjm.encryptmvp.domain;

import java.util.Map;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.util.UploadFileRequestBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zjm on 3/9/2017.
 */

public class UploadFileUseCase implements UseCase<String> {
    private BaseRepository mRepository;
    private String id;
    private Map<String, UploadFileRequestBody> fileMap;

    public UploadFileUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setFileMap(Map<String, UploadFileRequestBody> fileMap) {
        this.fileMap = fileMap;
    }

    @Override
    public Observable<String> execute() {
        return mRepository.uploadFile(id, fileMap);
    }
}
