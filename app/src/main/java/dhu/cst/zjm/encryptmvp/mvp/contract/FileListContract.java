package dhu.cst.zjm.encryptmvp.mvp.contract;

import java.util.List;

import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;

/**
 * Created by zjm on 3/2/2017.
 */

public interface FileListContract {
    interface View extends BaseView {

        void setUser(User user);

        void setupView();

        void getFileListNetworkError();

        void updateSourceList(List<ServerFile> list);

        void fileListOnClick(ServerFile serverFile);

    }

    interface Presenter extends BasePresenter<View> {
        void getMenuFileList(int id);

    }
}
