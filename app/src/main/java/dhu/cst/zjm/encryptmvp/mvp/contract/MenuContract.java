package dhu.cst.zjm.encryptmvp.mvp.contract;

import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;
import dhu.cst.zjm.encryptmvp.util.ProgressListener;

/**
 * Created by zjm on 3/3/2017.
 */

public interface MenuContract {
    interface View extends BaseView {
        void fileListClick(File file);

        void chooseFileError();

        void uploadNetworkError();

        void uploadSuccess();

        void uploadFailed();


    }

    interface Presenter extends BasePresenter<View> {
        void uploadFile(int id, java.io.File file, ProgressListener progressListener);
    }
}
