package dhu.cst.zjm.encryptmvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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
import dhu.cst.zjm.encryptmvp.injector.component.DaggerMenuFileListFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.component.MenuFileListFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.MenuFileListModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.MenuFileListContract;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.ui.adapter.MenuFileListAdapter;
import dhu.cst.zjm.encryptmvp.util.appbarlayout.SwipyAppBarScrollListener;

/**
 * Created by zjm on 3/2/2017.
 */

public class MenuFileListFragment extends BaseFragment implements MenuFileListContract.View {
    @Inject
    MenuFileListContract.Presenter menuContractPresenter;
    @BindView(R.id.rcv_menu_file_list)
    RecyclerView rcv_menu_file_list;
    @BindView(R.id.ptrv_menu_file_list)
    PullToRefreshView ptrv_menu_file_list;
    ImageView iv_menu_toolbar;
    AppBarLayout abl_ui_menu;
    CollapsingToolbarLayout ctl_menu;

    private User user;
    private List<ServerFile> sourceServerFileList;
    private MenuFileListAdapter menuFileListAdapter;

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
                        menuContractPresenter.getMenuFileList(user.getId());
                        ptrv_menu_file_list.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        sourceServerFileList = new ArrayList<ServerFile>();
        menuContractPresenter.getMenuFileList(user.getId());

        rcv_menu_file_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        menuFileListAdapter = new MenuFileListAdapter(getActivity(), sourceServerFileList);
        menuFileListAdapter.setOnItemClickListener(new MenuFileListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //menu_file_list_interface.fileListItemClick(sourceServerFileList.get(position));
            }
        });
        rcv_menu_file_list.setAdapter(menuFileListAdapter);
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
    public void updateSourceList(List<ServerFile> list) {
        sourceServerFileList.clear();
        for (ServerFile sf : list) {
            sourceServerFileList.add(sf);
        }
        menuFileListAdapter.notifyDataSetChanged();
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_menu_file_list;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getActivity().getApplication()).getApplicationComponent();
        MenuFileListFragmentComponent menuFileListFragmentComponent = DaggerMenuFileListFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .menuFileListModule(new MenuFileListModule())
                .build();
        menuFileListFragmentComponent.inject(this);

        menuContractPresenter.attachView(this);
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
