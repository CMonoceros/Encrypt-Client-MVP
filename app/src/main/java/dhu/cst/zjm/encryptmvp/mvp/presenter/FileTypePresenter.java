package dhu.cst.zjm.encryptmvp.mvp.presenter;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import dhu.cst.zjm.encryptmvp.domain.EncryptRelationUseCase;
import dhu.cst.zjm.encryptmvp.domain.ListEncryptTypeUseCase;
import dhu.cst.zjm.encryptmvp.domain.ResponseBodyUseCase;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.util.DownloadFileResponseBody;
import dhu.cst.zjm.encryptmvp.util.ProgressListener;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypePresenter implements FileTypeContract.Presenter {

    private FileTypeContract.View mView;
    private ListEncryptTypeUseCase mListEncryptTypeUseCase;
    private ResponseBodyUseCase mResponseBodyUseCase;
    private EncryptRelationUseCase mEncryptRelationUseCase;
    private CompositeSubscription mCompositeSubscription;
    private String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Encrypt/";

    public FileTypePresenter(ListEncryptTypeUseCase listEncryptTypeUseCase, ResponseBodyUseCase responseBodyUseCase, EncryptRelationUseCase encryptRelationUseCase) {
        this.mListEncryptTypeUseCase = listEncryptTypeUseCase;
        this.mResponseBodyUseCase = responseBodyUseCase;
        this.mEncryptRelationUseCase = encryptRelationUseCase;
    }

    @Override
    public void attachView(FileTypeContract.View BaseView) {
        this.mView = BaseView;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void detachView() {
        if (mCompositeSubscription != null && mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getEncryptType() {
        Subscription subscription = mListEncryptTypeUseCase.getEncryptType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EncryptType>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.getFileTypeNetworkError();
                    }

                    @Override
                    public void onNext(List<EncryptType> list) {
                        mView.updateEncryptType(list);
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void encryptFile(EncryptRelation encryptRelation) {
        switch (encryptRelation.getTypeId()) {
            case 1:
                mView.setDesKey(encryptRelation);
                break;
        }
    }

    @Override
    public void encryptBaseType(EncryptRelation encryptRelation, String desKey, String desLayer) {
        mEncryptRelationUseCase.setFileId(encryptRelation.getFileId() + "");
        mEncryptRelationUseCase.setTypeId(encryptRelation.getTypeId() + "");
        mEncryptRelationUseCase.setDesKey(desKey);
        mEncryptRelationUseCase.setDesLayer(desLayer);
        Subscription subscription = mEncryptRelationUseCase.encryptFile()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EncryptRelation>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.encryptBaseTypeNetworkError();
                    }

                    @Override
                    public void onNext(EncryptRelation encryptRelation) {
                        mView.encryptBaseTypeEncryptSuccess();
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void downloadFile(final dhu.cst.zjm.encryptmvp.mvp.model.File file, final ProgressListener listener) {
        String[] s = file.getName().split("\\.");
        final String realName = s[0];
        Subscription subscription = mResponseBodyUseCase.downloadFile(file.getId() + "")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, DownloadFileResponseBody>() {
                    @Override
                    public DownloadFileResponseBody call(ResponseBody responseBody) {
                        return new DownloadFileResponseBody(responseBody, listener);
                    }
                })
                .observeOn(Schedulers.computation())
                .map(new Func1<DownloadFileResponseBody, InputStream>() {
                    @Override
                    public InputStream call(DownloadFileResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<InputStream>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.downloadFileNetworkError();
                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        String dir = downloadPath + file.getOwner() + "/Save/";
                        File dirs = new File(dir);
                        File out = new File(dir + File.separator + realName + ".zip");
                        if (!out.exists() || !dirs.exists()) {
                            try {
                                dirs.mkdirs();
                                out.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
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
                                Log.i("Download Success ", "file download: " + fileSizeDownloaded);
                            }
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
