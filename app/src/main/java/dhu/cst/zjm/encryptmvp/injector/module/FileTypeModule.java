package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.GetEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.FileTypePresenter;

/**
 * Created by zjm on 2017/3/3.
 */

@Module
public class FileTypeModule {
    @Provides
    public GetEncryptTypeUseCase provideGetEncryptTypeUseCase(BaseRepository baseRepository) {
        return new GetEncryptTypeUseCase(baseRepository);
    }

    @Provides
    public FileTypeContract.Presenter provideFileTypePresenter(GetEncryptTypeUseCase getEncryptTypeUseCase) {
        return new FileTypePresenter(getEncryptTypeUseCase);
    }
}
