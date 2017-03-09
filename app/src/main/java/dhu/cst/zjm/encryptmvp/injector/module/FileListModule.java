package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.GetFileListUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileListContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.FileListPresenter;

/**
 * Created by zjm on 3/2/2017.
 */

@Module
public class FileListModule {
    @Provides
    public GetFileListUseCase provideGetFileListUseCase(BaseRepository baseRepository) {
        return new GetFileListUseCase(baseRepository);

    }

    @Provides
    public FileListContract.Presenter provideFileListPresenter(GetFileListUseCase getFileListUseCase) {
        return new FileListPresenter(getFileListUseCase);
    }
}
