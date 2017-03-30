package dhu.cst.zjm.encryptmvp.domain;

import java.util.List;

import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.base.BaseListEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import rx.Observable;

/**
 * Created by zjm on 2017/3/3.
 */

public class ListEncryptTypeUseCase implements BaseListEncryptTypeUseCase<List<EncryptType>> {

    private BaseRepository mBaseRepository;


    public ListEncryptTypeUseCase(BaseRepository baseRepository){
        this.mBaseRepository=baseRepository;
    }

    @Override
    public Observable<List<EncryptType>> getEncryptType() {
        return mBaseRepository.getEncryptType();
    }
}
