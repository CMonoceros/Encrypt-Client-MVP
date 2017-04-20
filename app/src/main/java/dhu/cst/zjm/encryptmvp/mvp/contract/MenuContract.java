package dhu.cst.zjm.encryptmvp.mvp.contract;

import android.content.Context;

import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;
import dhu.cst.zjm.encryptmvp.util.network.ProgressListener;

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

        void loadBackground();

        void uploadFileStartService(int owner, java.io.File file);

        void downloadFileStartService(File file);

        void uploadFileNeedWifi();

        void downloadFileNeedWifi();

        void downloadFileNetworkError();

        void downloadFileSuccess();

        void progressListenerShow();

        void notificationDismiss();

        void notificationShow();

        void jobSchedulerCancel();
    }

    interface Presenter extends BasePresenter<View> {

        void setupUploadService(int owner, java.io.File file,ProgressListener progressListener);

        void setupDownloadService(File file,ProgressListener progressListener);

        void uploadServiceWork();

        void downloadServiceWork();

        void progressListenerShow();
    }
}
