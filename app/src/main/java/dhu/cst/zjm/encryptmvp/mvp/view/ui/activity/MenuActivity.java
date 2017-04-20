package dhu.cst.zjm.encryptmvp.mvp.view.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerMenuActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.component.MenuActivityComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.MenuModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuContract;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.mvp.presenter.broadcastreceiver.NotificationReceiver;
import dhu.cst.zjm.encryptmvp.mvp.presenter.service.MenuJobService;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.fragment.FileListFragment;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.fragment.FileTypeFragment;
import dhu.cst.zjm.encryptmvp.util.FileUtil;
import dhu.cst.zjm.encryptmvp.util.IntentUtil;
import dhu.cst.zjm.encryptmvp.util.network.NetworkUtil;
import dhu.cst.zjm.encryptmvp.util.network.ProgressListener;

import static dhu.cst.zjm.encryptmvp.util.FileUtil.getPath;

/**
 * Created by zjm on 3/2/2017.
 */

public class MenuActivity extends BaseActivity implements MenuContract.View {
    private User user;
    private FragmentManager fm_menu_main;
    private FragmentTransaction ft_menu_main;
    private MenuJobService menuJobService;
    private JobScheduler jobScheduler;
    private int menuJobServiceID = 1;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public static final int JOB_SERVICE_CALL_BACK = 1;
    public static final int JOB_SERVICE_CANCEL_CALL_BACK = 2;

    public static final int NOTIFICATION_UPLOAD_OR_DOWNLOAD = 1;

    public static final int UPLOAD_SERVICE_TYPE = 1;
    public static final int DOWNLOAD_SERVICE_TYPE = 2;

    @Inject
    MenuContract.Presenter menuContractPresenter;
    @BindView(R.id.dl_ui_menu)
    DrawerLayout dl_ui_menu;
    @BindView(R.id.tb_menu_title)
    Toolbar tb_menu_title;
    @BindView(R.id.nv_menu_person)
    NavigationView nv_menu_person;
    @BindView(R.id.ctl_menu)
    CollapsingToolbarLayout ctl_menu;
    @BindView(R.id.rl_Menu_Main)
    RelativeLayout rl_Menu_Main;
    @BindView(R.id.iv_menu_toolbar)
    ImageView iv_menu_toolbar;
    ProgressDialog pd_file_progress;
    ProgressListener progressListener;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MenuActivity.JOB_SERVICE_CALL_BACK:
                    menuJobService = (MenuJobService) msg.obj;
                    menuJobService.setPresenter(menuContractPresenter);
                    break;
                case MenuActivity.JOB_SERVICE_CANCEL_CALL_BACK:
                    jobSchedulerCancel();
                    break;
            }
        }
    };

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_menu;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getApplication()).getApplicationComponent();
        MenuActivityComponent menuActivityComponent = DaggerMenuActivityComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(this))
                .menuModule(new MenuModule())
                .build();
        menuActivityComponent.inject(this);

        menuContractPresenter.attachView(this);
    }

    @Override
    protected void initButterKnife(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        menuJobService = new MenuJobService();
        loadBackground();
        user = (User) getIntent().getSerializableExtra(IntentUtil.EXTRA_MENU_USER);
        setupView();
        setupFragment();
        setupService();
        setupNotification();
        setupBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        jobScheduler.cancelAll();
    }

    private void setupBroadcastReceiver() {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setJobScheduler(jobScheduler);
    }

    private void setupService() {
        Intent startServiceIntent = new Intent(MenuActivity.this, MenuJobService.class);
        startServiceIntent.putExtra("Messenger", new Messenger(mHandler));
        startService(startServiceIntent);
    }

    private void setupView() {
        setSupportActionBar(tb_menu_title);

        ctl_menu.setTitle(user.getName());
        ctl_menu.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        ctl_menu.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        RelativeLayout nv_menu_header = (RelativeLayout) nv_menu_person.inflateHeaderView(R.layout.nv_menu_header);
        TextView tv_nv_menu_id = (TextView) nv_menu_header.findViewById(R.id.tv_nv_menu_id);
        TextView tv_nv_menu_name = (TextView) nv_menu_header.findViewById(R.id.tv_nv_menu_name);
        tv_nv_menu_id.setText(user.getId() + "");
        tv_nv_menu_name.setText(user.getName());


        nv_menu_person.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            private MenuItem mPreMenuItem;

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (mPreMenuItem != null) {
                    mPreMenuItem.setCheckable(false);
                }
                item.setChecked(true);
                dl_ui_menu.closeDrawers();
                mPreMenuItem = item;
                navigationViewClick(item);
                return true;
            }
        });

        pd_file_progress = new ProgressDialog(this);
        pd_file_progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd_file_progress.setIndeterminate(false);
        pd_file_progress.setProgress(0);
        pd_file_progress.setButton(DialogInterface.BUTTON_POSITIVE, "切入后台", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd_file_progress.dismiss();
            }
        });
        pd_file_progress.setCancelable(false);
        pd_file_progress.setCanceledOnTouchOutside(false);

        progressListener = new ProgressListener() {
            @Override
            public void onProgress(long progress, long total, boolean done) {
                pd_file_progress.setMax((int) total);
                pd_file_progress.setProgress((int) progress);
                notificationBuilder.setProgress((int) total, (int) progress, false);
                notificationShow();
            }
        };
    }

    private void setupFragment() {
        fm_menu_main = getFragmentManager();
        ft_menu_main = fm_menu_main.beginTransaction();
        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setUser(user);
        ft_menu_main.add(R.id.rl_Menu_Main, fileListFragment);
        ft_menu_main.commit();
    }

    private void setupNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setPriority(Notification.PRIORITY_MIN);
        notificationBuilder.setOngoing(true);
    }

    private void navigationViewClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nV_Menu_Item_Home:
                ft_menu_main = fm_menu_main.beginTransaction();
                FileListFragment fileListFragment = new FileListFragment();
                fileListFragment.setUser(user);
                ft_menu_main.replace(R.id.rl_Menu_Main, fileListFragment);
                ft_menu_main.addToBackStack(null);
                ft_menu_main.commit();
                break;
            case R.id.nV_Menu_Item_Upload_File:
                item.setChecked(false);
                IntentUtil.intentToChooseFile(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IntentUtil.EXTRA_CHOOSE_FILE:
                if (resultCode == Activity.RESULT_OK) {
                    String filePath = getPath(this, data.getData());
                    java.io.File file = new java.io.File(filePath);
                    if (!file.exists()) {
                        chooseFileError();
                    }
                    uploadFileStartService(user.getId(), file);
                } else {
                    chooseFileError();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            dl_ui_menu.openDrawer(GravityCompat.START);
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void fileListClick(File file) {
        ft_menu_main = fm_menu_main.beginTransaction();
        FileTypeFragment fileTypeFragment = new FileTypeFragment();
        fileTypeFragment.setFile(file);
        ft_menu_main.replace(R.id.rl_Menu_Main, fileTypeFragment);
        ft_menu_main.addToBackStack(null);
        ft_menu_main.commit();
    }

    @Override
    public void chooseFileError() {
        Toast.makeText(this, "Choose File Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadNetworkError() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Network Error");
        notificationDismiss();
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadSuccess() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Success upload");
        notificationDismiss();
        Toast.makeText(this, "Upload success!Please refresh list!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadFailed() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Failed upload");
        notificationDismiss();
        Toast.makeText(this, "Upload failed.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loadBackground() {
        nv_menu_person.setBackgroundColor(Color.WHITE);
        rl_Menu_Main.setBackgroundColor(Color.WHITE);

        Point point = new Point();
        WindowManager windowManager = getWindowManager();
        windowManager.getDefaultDisplay().getSize(point);
        int scrWidth = point.x;
        int scrHeight = 256;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.background_menu_toolbar, options);
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;

        int dx = picWidth / scrWidth;
        int dy = picHeight / scrHeight;
        int scale = 1;
        if (dx >= dy && dy >= 1) {
            scale = dx;
        }
        if (dy >= dx && dx >= 1) {
            scale = dy;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.background_menu_toolbar, options);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bmp);
        iv_menu_toolbar.setBackground(bitmapDrawable);
    }


    @Override
    public void uploadFileStartService(int owner, java.io.File file) {
        menuContractPresenter.setupUploadService(owner, file, progressListener);
        notificationBuilder.setContentTitle("Uploading " + file.getName());
        notificationBuilder.setSmallIcon(R.drawable.ic_file_upload_black_48dp);
        ComponentName componentName = new ComponentName(MenuActivity.this, MenuJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(menuJobServiceID, componentName);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("type", UPLOAD_SERVICE_TYPE);
        builder.setExtras(persistableBundle);
        double size = FileUtil.getFileOrFilesSize(file, FileUtil.SIZETYPE_MB);
        if (size > 1.0) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        }
        builder.setMinimumLatency(100);
        if (NetworkUtil.isMobileConnected(this)) {
            uploadFileNeedWifi();
        }
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void downloadFileStartService(File file) {
        menuContractPresenter.setupDownloadService(file, progressListener);
        notificationBuilder.setContentTitle("Downloading " + file.getName());
        notificationBuilder.setSmallIcon(R.drawable.ic_file_download_black_48dp);
        ComponentName componentName = new ComponentName(MenuActivity.this, MenuJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(menuJobServiceID, componentName);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putInt("type", DOWNLOAD_SERVICE_TYPE);
        builder.setExtras(persistableBundle);
        String size = file.getSize();
        if (!size.endsWith("KB")) {
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        }
        builder.setMinimumLatency(100);
        if (NetworkUtil.isMobileConnected(this)) {
            downloadFileNeedWifi();
        }
        jobScheduler.schedule(builder.build());
    }

    @Override
    public void uploadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to upload!",
                Toast.LENGTH_SHORT).show();
        notificationBuilder.setContentIntent(setupPendingIntent());
        notificationBuilder.setContentTitle("Waiting to connect WIFI!");
        notificationBuilder.setSmallIcon(R.drawable.ic_file_upload_black_48dp);
        notificationShow();
    }

    @Override
    public void downloadFileNeedWifi() {
        Toast.makeText(this, "File is too big!Need Wifi to download!",
                Toast.LENGTH_SHORT).show();
        notificationBuilder.setContentIntent(setupPendingIntent());
        notificationBuilder.setSmallIcon(R.drawable.ic_file_download_black_48dp);
        notificationBuilder.setContentTitle("Waiting to connect WIFI!");
        notificationShow();
    }

    private PendingIntent setupPendingIntent() {
        Intent clickIntent = new Intent(MenuActivity.this, NotificationReceiver.class);
        clickIntent.putExtra("jobServiceID", menuJobServiceID);
        clickIntent.putExtra("Messenger", new Messenger(mHandler));
        clickIntent.setAction("dhu.cst.zjm.start_job");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    @Override
    public void downloadFileNetworkError() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Network Error");
        notificationDismiss();
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void downloadFileSuccess() {
        pd_file_progress.dismiss();
        notificationBuilder.setContentTitle("Success download");
        notificationDismiss();
        Toast.makeText(this, "Download success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void progressListenerShow() {
        pd_file_progress.show();
    }

    @Override
    public void notificationDismiss() {
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setOngoing(false);
        notificationShow();
    }

    @Override
    public void notificationShow() {
        Notification notification = notificationBuilder.build();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notificationManager.notify(NOTIFICATION_UPLOAD_OR_DOWNLOAD, notification);
    }

    @Override
    public void jobSchedulerCancel() {
        notificationBuilder.setContentTitle("Cancel");
        notificationDismiss();
    }
}
