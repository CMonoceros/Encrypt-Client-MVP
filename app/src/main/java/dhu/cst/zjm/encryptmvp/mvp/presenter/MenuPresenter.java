package dhu.cst.zjm.encryptmvp.mvp.presenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import dhu.cst.zjm.encryptmvp.domain.FileUseCase;
import dhu.cst.zjm.encryptmvp.domain.ResponseBodyUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.util.FileUtil;
import dhu.cst.zjm.encryptmvp.util.network.DownloadFileResponseBody;
import dhu.cst.zjm.encryptmvp.util.network.ProgressListener;
import dhu.cst.zjm.encryptmvp.util.network.UploadFileRequestBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2/9/2017.
 */

public class MenuPresenter implements MenuContract.Presenter {
    private FileUseCase mFileUseCase;
    private ResponseBodyUseCase mResponseBodyUseCase;
    private MenuContract.View mView;
    private CompositeSubscription mCompositeSubscription;
    private int owner;
    private File uploadFile;
    private dhu.cst.zjm.encryptmvp.mvp.model.File downloadFile;
    private ProgressListener progressListener;


    public MenuPresenter(FileUseCase fileUseCase, ResponseBodyUseCase responseBodyUseCase) {
        this.mFileUseCase = fileUseCase;
        this.mResponseBodyUseCase = responseBodyUseCase;
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
    public void setupUploadService(int owner, File file, ProgressListener progressListener) {
        this.owner = owner;
        this.uploadFile = file;
        this.progressListener = progressListener;
    }

    @Override
    public void setupDownloadService(dhu.cst.zjm.encryptmvp.mvp.model.File file, ProgressListener progressListener) {
        this.downloadFile = file;
        this.progressListener = progressListener;
    }

    @Override
    public void uploadServiceWork() {
        UploadFileRequestBody fileBody = new UploadFileRequestBody(uploadFile, progressListener);
        Map<String, RequestBody> map = new HashMap<>();
        map.put("file\"; filename=\"" + uploadFile.getName(), fileBody);
        map.put("owner", RequestBody.create(MediaType.parse("text/plain"), owner + ""));
        map.put("fileName", RequestBody.create(MediaType.parse("text/plain"), uploadFile.getName()));
        map.put("fileSize", RequestBody.create(MediaType.parse("text/plain"), FileUtil.getAutoFileOrFilesSize(uploadFile.getPath())));
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

    @Override
    public void downloadServiceWork() {
        final dhu.cst.zjm.encryptmvp.mvp.model.File f = downloadFile;
        final ProgressListener pl = progressListener;
        String[] s = downloadFile.getName().split("\\.");
        final String realName = s[0];
        Subscription subscription = mResponseBodyUseCase.downloadFile(downloadFile.getId() + "")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return new DownloadFileResponseBody(responseBody, pl).byteStream();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<InputStream, Boolean>() {
                    @Override
                    public Boolean call(InputStream inputStream) {
                        String dir = FileUtil.createDir(FileTypePresenter.downloadPath + f.getOwner() + "/Save/");
                        File out = new File(dir + File.separator + realName + ".zip");
                        if (!out.exists()) {
                            try {
                                out.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return false;
                            }
                        }
                        try {
                            OutputStream outputStream = null;
                            byte[] fileReader = new byte[4096];
                            long fileSizeDownloaded = 0;
                            outputStream = new FileOutputStream(out);
                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                            }
                            outputStream.flush();
                            outputStream.close();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.downloadFileNetworkError();
                    }

                    @Override
                    public void onNext(Boolean state) {
                        if (state) {
                            mView.downloadFileSuccess();
                        } else {
                            mView.downloadFileNetworkError();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void progressListenerShow() {
        mView.progressListenerShow();
    }
}
