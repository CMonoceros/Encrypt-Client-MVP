package dhu.cst.zjm.encryptmvp.domain;

import java.util.Map;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseFileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by zjm on 3/9/2017.
 */

public class FileUseCase implements BaseFileUseCase<File> {
    private BaseRepository mRepository;
    private  Map<String,RequestBody> fileMap;


    public FileUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }


    public void setFileMap( Map<String,RequestBody> fileMap) {
        this.fileMap=fileMap;
    }


    @Override
    public Observable<File> uploadFile() {
        return mRepository.uploadFile(fileMap);
    }
}
