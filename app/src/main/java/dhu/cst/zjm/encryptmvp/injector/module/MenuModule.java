package dhu.cst.zjm.encryptmvp.injector.module;

import dagger.Module;
import dagger.Provides;
import dhu.cst.zjm.encryptmvp.api.repository.BaseRepository;
import dhu.cst.zjm.encryptmvp.domain.FileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.mvp.presenter.MenuPresenter;

/**
 * Created by zjm on 2/9/2017.
 */

@Module
public class MenuModule {
    @Provides
    public FileUseCase provideFileUseCase(BaseRepository baseRepository) {
        return new FileUseCase(baseRepository);
    }

    @Provides
    public MenuContract.Presenter provideMenuPresenter(FileUseCase fileUseCase) {
        return new MenuPresenter(fileUseCase);
    }
}
