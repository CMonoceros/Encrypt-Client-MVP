package dhu.cst.zjm.encryptmvp.mvp.presenter;

import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;

/**
 * Created by zjm on 2017/2/24.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T BaseView);

    void detachView();
}
