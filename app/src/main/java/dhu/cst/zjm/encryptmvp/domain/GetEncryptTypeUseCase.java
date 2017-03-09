package dhu.cst.zjm.encryptmvp.domain;

import java.util.List;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import rx.Observable;

/**
 * Created by zjm on 2017/3/3.
 */

public class GetEncryptTypeUseCase implements UseCase<List<EncryptType>>{

    private BaseRepository mBaseRepository;

    public GetEncryptTypeUseCase(BaseRepository baseRepository){
        this.mBaseRepository=baseRepository;
    }
    @Override
    public Observable<List<EncryptType>> execute() {
        return mBaseRepository.getEncryptType();
    }
}
