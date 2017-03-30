package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.ListFileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileListContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.FileListPresenter;

/**
 * Created by zjm on 3/2/2017.
 */

@Module
public class FileListModule {
    @Provides
    public ListFileUseCase provideListFileUseCase(BaseRepository baseRepository) {
        return new ListFileUseCase(baseRepository);

    }

    @Provides
    public FileListContract.Presenter provideFileListPresenter(ListFileUseCase listFileUseCase) {
        return new FileListPresenter(listFileUseCase);
    }
}
