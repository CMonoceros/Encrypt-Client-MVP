package dhu.cst.zjm.encryptmvp.mvp.contract;

import android.graphics.Bitmap;

import dhu.cst.zjm.encryptmvp.mvp.presenter.BasePresenter;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;

/**
 * Created by zjm on 2017/3/1.
 */

public interface RegisterContract {
    interface View extends BaseView {
        void confirmError();

        void registerSuccess(int id);

        void registerNetworkError();

        void registerEmptyError();

        void registerVerificationError();

        void setVerificationBitmap(Bitmap bitmap);
    }

    interface Presenter extends BasePresenter<RegisterContract.View> {

        void generateVerification();

        void registerTry(String name, String password, String confirmPassword,String verification);
    }
}
