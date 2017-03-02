package dhu.cst.zjm.encryptmvp.domain;

import java.util.List;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.api.repository.Repository;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import rx.Observable;

/**
 * Created by zjm on 3/2/2017.
 */

public class GetMenuFileListUseCase implements UseCase<List<ServerFile>> {
    private BaseRepository mRepository;
    private String id;

    public GetMenuFileListUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Observable<List<ServerFile>> execute() {
        return mRepository.getMenuFileList(id);
    }
}
