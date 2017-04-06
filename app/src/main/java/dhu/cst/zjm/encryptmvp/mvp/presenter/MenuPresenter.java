package dhu.cst.zjm.encryptmvp.mvp.presenter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.domain.FileUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.util.FileUtil;
import dhu.cst.zjm.encryptmvp.util.web.ProgressListener;
import dhu.cst.zjm.encryptmvp.util.web.UploadFileRequestBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2/9/2017.
 */

public class MenuPresenter implements MenuContract.Presenter {
    private FileUseCase mFileUseCase;
    private MenuContract.View mView;
    private CompositeSubscription mCompositeSubscription;

    public MenuPresenter(FileUseCase fileUseCase) {
        this.mFileUseCase = fileUseCase;
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
    public void uploadFile(int owner, File file, ProgressListener progressListener) {
        UploadFileRequestBody fileBody = new UploadFileRequestBody(file, progressListener);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + file.getName(), fileBody);
        map.put("owner", RequestBody.create(MediaType.parse("text/plain"), owner + ""));
        map.put("fileName", RequestBody.create(MediaType.parse("text/plain"), file.getName()));
        map.put("fileSize", RequestBody.create(MediaType.parse("text/plain"), FileUtil.getAutoFileOrFilesSize(file.getPath())));
        mFileUseCase.setFileMap(map);
        //使用RxJava方式调度任务并监听
        Subscription subscription = mFileUseCase.uploadFile()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<dhu.cst.zjm.encryptmvp.mvp.model.File>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.uploadNetworkError();
                    }

                    @Override
                    public void onNext(dhu.cst.zjm.encryptmvp.mvp.model.File file) {
                        if (file == null) {
                            mView.uploadFailed();
                        } else {
                            mView.uploadSuccess();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);

    }
}
