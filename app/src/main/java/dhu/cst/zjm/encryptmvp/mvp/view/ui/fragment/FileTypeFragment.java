package dhu.cst.zjm.encryptmvp.mvp.view.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptRelation;
import dhu.cst.zjm.encryptmvp.mvp.model.EncryptType;
import dhu.cst.zjm.encryptmvp.mvp.model.File;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.activity.MenuActivity;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.adapter.FileTypeAdapter;

/**
 * Created by zjm on 2017/3/3.
 */

public class FileTypeFragment extends BaseFragment implements FileTypeContract.View {

    @Inject
    FileTypeContract.Presenter fileTypePresenter;
    private List<EncryptType> sourceEncryptTypeList;
    private File file;
    private FileTypeAdapter fileTypeAdapter;

    @BindView(R.id.rcv_menu_file_type)
    public RecyclerView rcv_menu_file_type;
    public android.support.v7.app.AlertDialog.Builder adb_menu_file_encrypt, adb_others;

    private CollapsingToolbarLayout ctl_menu;
    private ImageView iv_menu_toolbar;
    private TextView tv_menu_toolbar;
    private EditText et_menu_file_encrypt_exinf;
    private View v_menu_file_encrypt;
    private ViewGroup vg_menu_file_encrypt;

    @Override
    public void updateEncryptType(List<EncryptType> list) {
        sourceEncryptTypeList.clear();
        for (int i=0;i<list.size();i++) {
            sourceEncryptTypeList.add(list.get(i));
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
        adb_others.setMessage(encryptType.getDescription());
        adb_others.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb_others.show();
    }

    public void setFile(File file) {
        this.file = file;
    }


    @Override
    public void setDesKey(final EncryptRelation encryptRelation) {
        setupEncryptExinfDialog();
        adb_menu_file_encrypt.setTitle("输入DES密钥！");
        adb_menu_file_encrypt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String i = et_menu_file_encrypt_exinf.getText().toString();
                if (i.length() < 8) {
                    Toast.makeText(getActivity(), "DES Key at least 8 bit!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    fileTypePresenter.encryptBaseType(encryptRelation, i, "1");
                }
            }
        });
        adb_menu_file_encrypt.show();
    }

    @Override
    public void encryptBaseTypeNetworkError() {
        Toast.makeText(getActivity(), "Network Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void encryptBaseTypeEncryptSuccess() {
        Toast.makeText(getActivity(), "Encrypt File Success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptFileExistError() {
        Toast.makeText(getActivity(), "File doesn't exist!Please download first!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptSuccess() {
        Toast.makeText(getActivity(), "Decrypt File Success!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptError() {
        Toast.makeText(getActivity(), "Decrypt Error.Please try again later!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void decryptBaseTypeDecryptFailed() {
        Toast.makeText(getActivity(), "Decrypt Failed.Please confirm your key!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmDesKey(EncryptRelation encryptRelation) {
        setupEncryptExinfDialog();
        adb_menu_file_encrypt.setTitle("输入DES密钥！");
        adb_menu_file_encrypt.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String i = et_menu_file_encrypt_exinf.getText().toString();
                if (i.length() < 8) {
                    Toast.makeText(getActivity(), "DES Key at least 8 bit!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    fileTypePresenter.decryptBaseType(file,i);
                }
            }
        });
        adb_menu_file_encrypt.show();
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
                MenuActivity menuActivity=(MenuActivity)getActivity();
                menuActivity.downloadFileStartService(file);
            }
        });
        fileTypeAdapter.setEncryptClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptRelation encryptRelation = new EncryptRelation();
                encryptRelation.setFileId(file.getId());
                encryptRelation.setTypeId(sourceEncryptTypeList.get(position).getId());
                fileTypePresenter.encryptFile(encryptRelation);
            }
        });
        fileTypeAdapter.setClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                typeDetailClick(sourceEncryptTypeList.get(position));
            }
        });
        fileTypeAdapter.setDecryptClickListener(new FileTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                EncryptRelation encryptRelation = new EncryptRelation();
                encryptRelation.setFileId(file.getId());
                encryptRelation.setTypeId(sourceEncryptTypeList.get(position).getId());
                fileTypePresenter.decryptFile(encryptRelation, file);
            }
        });

        rcv_menu_file_type.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rcv_menu_file_type.setAdapter(fileTypeAdapter);
    }

    private void setupActivityView() {
        iv_menu_toolbar = (ImageView) getActivity().findViewById(R.id.iv_menu_toolbar);
        iv_menu_toolbar.setVisibility(View.GONE);

        ctl_menu = (CollapsingToolbarLayout) getActivity().findViewById(R.id.ctl_menu);
        ctl_menu.setTitle(file.getName());

        tv_menu_toolbar = (TextView) getActivity().findViewById(R.id.tv_menu_toolbar);
        tv_menu_toolbar.setText(file.getSize() + "\n \n" + file.getUploadTime());
    }

    private void setupEncryptExinfDialog() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        vg_menu_file_encrypt = (ViewGroup) getActivity().findViewById(R.id.rl_menu_file_encrypt_exinf);
        v_menu_file_encrypt = inflater.inflate(R.layout.ui_menu_file_encrypt_exinf, vg_menu_file_encrypt);
        et_menu_file_encrypt_exinf = (EditText) v_menu_file_encrypt.findViewById(R.id.et_menu_file_encrypt_exinf);
        adb_menu_file_encrypt = new android.support.v7.app.AlertDialog.Builder(getActivity());
        adb_menu_file_encrypt.setView(v_menu_file_encrypt);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupView();
        setupActivityView();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyApplication.getmRefWatcher(getActivity()).watch(this);
    }
}
