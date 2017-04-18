package dhu.cst.zjm.encryptmvp.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import dhu.cst.zjm.encryptmvp.ui.fragment.FileListFragment;
import dhu.cst.zjm.encryptmvp.ui.fragment.FileTypeFragment;
import dhu.cst.zjm.encryptmvp.util.IntentUtil;
import dhu.cst.zjm.encryptmvp.util.web.ProgressListener;

import static dhu.cst.zjm.encryptmvp.util.FileUtil.getPath;

/**
 * Created by zjm on 3/2/2017.
 */

public class MenuActivity extends BaseActivity implements MenuContract.View {
    private User user;
    private FragmentManager fm_menu_main;
    private FragmentTransaction ft_menu_main;

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
    ProgressDialog pd_file_progress;

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
        dl_ui_menu.setBackgroundColor(Color.WHITE);
        user = (User) getIntent().getSerializableExtra(IntentUtil.EXTRA_MENU_USER);
        setupView();
        setupFragment();
    }

    private void setupView() {
        setSupportActionBar(tb_menu_title);

        ctl_menu.setTitle(user.getName());
        ctl_menu.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        ctl_menu.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        RelativeLayout nv_menu_header = (RelativeLayout) nv_menu_person.inflateHeaderView(R.layout.nv_menu_header);
        nv_menu_person.removeHeaderView(nv_menu_person.getHeaderView(0));
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
        pd_file_progress.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        pd_file_progress.setCancelable(false);
        pd_file_progress.setCanceledOnTouchOutside(false);
    }

    private void setupFragment() {
        fm_menu_main = getFragmentManager();
        ft_menu_main = fm_menu_main.beginTransaction();
        FileListFragment fileListFragment = new FileListFragment();
        fileListFragment.setUser(user);
        ft_menu_main.add(R.id.rl_Menu_Main, fileListFragment);
        ft_menu_main.commit();
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
                    menuContractPresenter.uploadFile(user.getId(), file, new ProgressListener() {
                        @Override
                        public void onProgress(long progress, long total, boolean done) {
                            pd_file_progress.setMax((int) total);
                            pd_file_progress.setProgress((int) progress);
                        }
                    });
                    pd_file_progress.show();
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
            return true;
        }
        return true;
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
        pd_file_progress.dismiss();
        Toast.makeText(this, "Choose File Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadNetworkError() {
        pd_file_progress.dismiss();
        Toast.makeText(this, "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadSuccess() {
        Toast.makeText(this, "Upload success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void uploadFailed() {
        pd_file_progress.dismiss();
        Toast.makeText(this, "Upload failed.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }
}
