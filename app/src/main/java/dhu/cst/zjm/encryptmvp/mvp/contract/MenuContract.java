package dhu.cst.zjm.encryptmvp.mvp.contract;

import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;
import dhu.cst.zjm.encryptmvp.util.ProgressListener;

/**
 * Created by zjm on 3/3/2017.
 */

public interface MenuContract {
    interface View extends BaseView {
        void fileListClick(ServerFile serverFile);

        void chooseFileError();

        void uploadNetworkError();

        void uploadSuccess();


    }

    interface Presenter extends BasePresenter<View> {
        void uploadFile(int id, String filePath,ProgressListener progressListener);
    }
}
