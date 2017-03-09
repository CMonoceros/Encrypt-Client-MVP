package dhu.cst.zjm.encryptmvp.mvp.contract;

import java.util.List;

import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;

/**
 * Created by zjm on 2017/3/3.
 */

public interface FileTypeContract {
    interface View extends BaseView {
        void updateEncryptType(List<EncryptType> list);

        void getFileTypeNetworkError();

        void typeDetailClick(EncryptType encryptType);

        void setServerFile(ServerFile serverFile);
    }

    interface Presenter extends BasePresenter<View> {
        void getEncryptType();
    }
}
