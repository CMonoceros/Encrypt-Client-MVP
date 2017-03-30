package dhu.cst.zjm.encryptmvp.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseListFileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import rx.Observable;

/**
 * Created by zjm on 3/2/2017.
 */

public class ListFileUseCase implements BaseListFileUseCase<List<File>> {
    private BaseRepository mRepository;
    private String owner;
    private Map<String, String> params=new HashMap<>();

    public ListFileUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }

    public void setOwner(String id) {
        this.owner = id;
        params.put("owner",id);
    }

    public void setRows(String rows){
        params.put("rows",rows);
    }

    public void setPaper(String paper){
        params.put("paper",paper);
    }

    @Override
    public Observable<List<File>> getFileList() {
        return mRepository.getFileList(owner);
    }

    @Override
    public Observable<List<File>> getFileListByPaper() {
        return mRepository.getFileListByPaper(params);
    }
}
