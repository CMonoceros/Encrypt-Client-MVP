package dhu.cst.zjm.encryptmvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerFileListFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.component.FileListFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.FileListModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileListContract;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.ui.activity.MenuActivity;
import dhu.cst.zjm.encryptmvp.ui.adapter.FileListAdapter;
import dhu.cst.zjm.encryptmvp.util.appbarlayout.SwipyAppBarScrollListener;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListFragment extends BaseFragment implements FileListContract.View {

    @Inject
    FileListContract.Presenter fileListPresenter;
    @BindView(R.id.rcv_menu_file_list)
    RecyclerView rcv_menu_file_list;
    @BindView(R.id.ptrv_menu_file_list)
    PullToRefreshView ptrv_menu_file_list;
    AppBarLayout abl_ui_menu;
    ImageView iv_menu_toolbar;
    CollapsingToolbarLayout ctl_menu;

    private User user;
    private List<File> sourceFileList;
    private FileListAdapter fileListAdapter;
    private int fileListRows;
    private int fileListPaper;

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setupView() {
        ptrv_menu_file_list.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ptrv_menu_file_list.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fileListPresenter.getMenuFileList(user.getId());
                        ptrv_menu_file_list.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        sourceFileList = new ArrayList<File>();
        fileListPresenter.getMenuFileList(user.getId());

        rcv_menu_file_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        fileListAdapter = new FileListAdapter(getActivity(), sourceFileList);
        fileListAdapter.setOnItemClickListener(new FileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                fileListOnClick(sourceFileList.get(position));
            }
        });
        rcv_menu_file_list.setAdapter(fileListAdapter);
    }

    private void setupActivityView() {
        abl_ui_menu = (AppBarLayout) getActivity().findViewById(R.id.abl_ui_menu);

        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.VISIBLE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setContentScrimColor(getResources().getColor(R.color.transparent));
        ctl_menu.setTitle(user.getName());

        rcv_menu_file_list.addOnScrollListener(new SwipyAppBarScrollListener(abl_ui_menu, ptrv_menu_file_list, rcv_menu_file_list));
    }

    @Override
    public void getFileListNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSourceList(List<File> list) {
        sourceFileList.clear();
        for (File sf : list) {
            sourceFileList.add(sf);
        }
        fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public void fileListOnClick(File file) {
        MenuActivity menuActivity=(MenuActivity)getActivity();
        menuActivity.fileListClick(file);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_menu_file_list;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getActivity().getApplication()).getApplicationComponent();
        FileListFragmentComponent fileListFragmentComponent = DaggerFileListFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .fileListModule(new FileListModule())
                .build();
        fileListFragmentComponent.inject(this);

        fileListPresenter.attachView(this);
    }

    @Override
    protected void initButterKnife(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupView();
        setupActivityView();
        return view;

    }


}
