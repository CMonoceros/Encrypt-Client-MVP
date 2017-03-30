package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.EncryptRelationUseCase;
import dhu.cst.zjm.encryptmvp.domain.ListEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.domain.ResponseBodyUseCase;
import dhu.cst.zjm.encryptmvp.domain.base.BaseResponseBodyUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.FileTypePresenter;

/**
 * Created by zjm on 2017/3/3.
 */

@Module
public class FileTypeModule {
    @Provides
    public ListEncryptTypeUseCase provideListEncryptTypeUseCase(BaseRepository baseRepository) {
        return new ListEncryptTypeUseCase(baseRepository);
    }

    @Provides
    public ResponseBodyUseCase provideResponseBodyUseCase(BaseRepository baseRepository){
        return new ResponseBodyUseCase(baseRepository);
    }

    @Provides
    public EncryptRelationUseCase provideEncryptRelationUseCase(BaseRepository baseRepository){
        return new EncryptRelationUseCase(baseRepository);
    }

    @Provides
    public FileTypeContract.Presenter provideFileTypePresenter(ListEncryptTypeUseCase listEncryptTypeUseCase,ResponseBodyUseCase responseBodyUseCase,EncryptRelationUseCase encryptRelationUseCase) {
        return new FileTypePresenter(listEncryptTypeUseCase,responseBodyUseCase,encryptRelationUseCase);
    }
}
