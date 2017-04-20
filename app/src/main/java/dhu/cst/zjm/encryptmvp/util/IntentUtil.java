package dhu.cst.zjm.encryptmvp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import dhu.cst.zjm.encryptmvp.mvp.model.User;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.activity.MenuActivity;
import dhu.cst.zjm.encryptmvp.mvp.view.ui.activity.RegisterActivity;

/**
 * Created by zjm on 3/2/2017.
 */

public class IntentUtil {

    public static final String EXTRA_MENU_USER = "extra_menu_user";
    public static final int EXTRA_CHOOSE_FILE = 1;

    public static final void intentToRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        activity.startActivity(intent);
        //过渡动画
        activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                android.support.v7.appcompat.R.anim.abc_fade_out);
    }

    public static final void intentToMenuActivity(Activity activity, User user) {
        Intent intent = new Intent(activity, MenuActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_MENU_USER, user);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.finish();
        //过渡动画
        activity.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_fade_in,
                android.support.v7.appcompat.R.anim.abc_fade_out);
    }

    public static final void intentToChooseFile(Activity activity) {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        activity.startActivityForResult(chooseFile, EXTRA_CHOOSE_FILE);
    }
}
