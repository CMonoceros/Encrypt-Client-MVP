package dhu.cst.zjm.encryptmvp.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhu.cst.zjm.encryptmvp.MyApplication;
import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.injector.component.ApplicationComponent;
import dhu.cst.zjm.encryptmvp.injector.component.DaggerFileTypeFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.component.FileTypeFragmentComponent;
import dhu.cst.zjm.encryptmvp.injector.module.ActivityModule;
import dhu.cst.zjm.encryptmvp.injector.module.FileTypeModule;
import dhu.cst.zjm.encryptmvp.mvp.contract.FileTypeContract;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.ServerFile;
import dhu.cst.zjm.encryptmvp.ui.adapter.FileTypeAdapter;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypeFragment extends BaseFragment implements FileTypeContract.View {

    @Inject
    FileTypeContract.Presenter fileTypePresenter;
    private List<EncryptType> sourceEncryptTypeList;
    private ServerFile serverFile;
    private FileTypeAdapter fileTypeAdapter;

    @BindView(R.id.rcv_menu_file_type)
    public RecyclerView rcv_menu_file_type;
    public android.support.v7.app.AlertDialog.Builder adb_others;

    private CollapsingToolbarLayout ctl_menu;
    private ImageView iv_menu_toolbar;
    private TextView tv_menu_toolbar;

    @Override
    public void updateEncryptType(List<EncryptType> list) {
        sourceEncryptTypeList.clear();
        for (EncryptType e : list) {
            sourceEncryptTypeList.add(e);
        }
        fileTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public void getFileTypeNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void typeDetailClick(EncryptType encryptType) {
        adb_others.setTitle(encryptType.getName());
        adb_others.setMessage(encryptType.getInf());
        adb_others.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb_others.show();
    }

    @Override
    public void setServerFile(ServerFile serverFile) {
        this.serverFile = serverFile;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.ui_menu_file_type;
    }

    @Override
    protected void injectDependences() {
        ApplicationComponent applicationComponent = ((MyApplication) getActivity().getApplication()).getApplicationComponent();
        FileTypeFragmentComponent fileTypeFragmentComponent = DaggerFileTypeFragmentComponent.builder()
                .applicationComponent(applicationComponent)
                .activityModule(new ActivityModule(getActivity()))
                .fileTypeModule(new FileTypeModule())
                .build();
        fileTypeFragmentComponent.inject(this);

        fileTypePresenter.attachView(this);
    }

    @Override
    protected void initButterKnife(View rootView) {
        ButterKnife.bind(this, rootView);
    }

    private void setupView() {
        adb_others = new android.support.v7.app.AlertDialog.Builder(getActivity());

        sourceEncryptTypeList = new ArrayList<EncryptType>();
        fileTypePresenter.getEncryptType();

        fileTypeAdapter = new FileTypeAdapter(getActivity(), sourceEncryptTypeList);
        fileTypeAdapter.setMode(Attributes.Mode.Single);
        fileTypeAdapter.setDownloadClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        fileTypeAdapter.setRightClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });
        fileTypeAdapter.setClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //menu_file_type_interface.detailsClick(sourceEncryptTypeList.get(position));
            }
        });

        fileTypeAdapter.setDecryptClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

        rcv_menu_file_type.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rcv_menu_file_type.setAdapter(fileTypeAdapter);
    }

    private void setupActivityView() {
        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.INVISIBLE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        ctl_menu.setTitle(serverFile.getName());

        tv_menu_toolbar = (TextView) getActivity().findViewById(R.id.tv_menu_toolbar);
        tv_menu_toolbar.setText(serverFile.getSize() + "\n \n" + serverFile.getUploadTime());
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
