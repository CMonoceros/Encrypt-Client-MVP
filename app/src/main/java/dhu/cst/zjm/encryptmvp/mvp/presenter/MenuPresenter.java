package dhu.cst.zjm.encryptmvp.mvp.presenter;

import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.domain.UploadFileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.mvp.view.BaseView;
import dhu.cst.zjm.encryptmvp.util.ProgressListener;
import dhu.cst.zjm.encryptmvp.util.UploadFileRequestBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2/9/2017.
 */

public class MenuPresenter implements MenuContract.Presenter {
    private UploadFileUseCase mUploadFileUseCase;
    private MenuContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public MenuPresenter(UploadFileUseCase uploadFileUseCase) {
        this.mUploadFileUseCase = uploadFileUseCase;
    }

    @Override
    public void attachView(MenuContract.View BaseView) {
        mView = BaseView;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void uploadFile(int id, String filePath, ProgressListener progressListener) {
        mUploadFileUseCase.setID(id + "");
        File file = new File(filePath);
        Map<String, UploadFileRequestBody> partMap = new HashMap<>();
        UploadFileRequestBody fileBody = new UploadFileRequestBody(file, progressListener);
        partMap.put("file\"; filename=\"" + file.getName() + "\"", fileBody);
        mUploadFileUseCase.setFileMap(partMap);
        //使用RxJava方式调度任务并监听
        Subscription subscription = mUploadFileUseCase.execute()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.uploadNetworkError();
                    }

                    @Override
                    public void onNext(String uploadFile) {
                        mView.uploadSuccess();
                    }
                });
        mCompositeSubscription.add(subscription);

    }
}
