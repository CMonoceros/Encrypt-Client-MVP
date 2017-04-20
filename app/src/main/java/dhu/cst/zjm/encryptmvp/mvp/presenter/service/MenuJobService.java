package dhu.cst.zjm.encryptmvp.mvp.presenter.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;

import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.activity.MenuActivity;

/**
 * Created by zjm on 2017/4/19.
 */

public class MenuJobService extends android.app.job.JobService {
    MenuContract.Presenter menuContractPresenter;

    public void setPresenter(MenuContract.Presenter presenter) {
        this.menuContractPresenter = presenter;
    }

    public MenuJobService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Messenger callback = intent.getParcelableExtra("Messenger");
        Message m = Message.obtain();
        m.what = MenuActivity.JOB_SERVICE_CALL_BACK;
        m.obj = this;
        try {
            callback.send(m);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }


    @Override
    public boolean onStartJob(JobParameters params) {
        menuContractPresenter.progressListenerShow();
        PersistableBundle persistableBundle = params.getExtras();
        int type = persistableBundle.getInt("type");
        switch (type) {
            case MenuActivity.UPLOAD_SERVICE_TYPE:
                menuContractPresenter.uploadServiceWork();
                break;
            case MenuActivity.DOWNLOAD_SERVICE_TYPE:
                menuContractPresenter.downloadServiceWork();
                break;
        }
        jobFinished(params, false);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
