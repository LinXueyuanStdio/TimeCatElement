package com.timecat.element.alert;

import android.content.Context;
import android.widget.Toast;

import com.timecat.extend.arms.BaseApplication;

import androidx.annotation.StringRes;
import es.dmoral.toasty.Toasty;

public class ToastUtil {
    private static String getString(@StringRes int message) {
        return BaseApplication.getContext().getResources().getString(message);
    }

    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, @StringRes int rid) {
        Toast.makeText(context, rid, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, @StringRes int rid) {
        Toast.makeText(context, rid, Toast.LENGTH_LONG).show();
    }

    public static void i(Context context, String message) {
        Toasty.info(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void e(Context context, String message) {
        Toasty.error(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void w(Context context, String message) {
        Toasty.warning(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void ok(Context context, String message) {
        Toasty.success(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void i_long(Context context, String message) {
        Toasty.info(context, message, Toast.LENGTH_LONG).show();

    }

    public static void e_long(Context context, String message) {
        Toasty.error(context, message, Toast.LENGTH_LONG).show();
    }

    public static void w_long(Context context, String message) {
        Toasty.warning(context, message, Toast.LENGTH_LONG).show();
    }

    public static void ok_long(Context context, String message) {
        Toasty.success(context, message, Toast.LENGTH_LONG).show();
    }

    public static void i(Context context, @StringRes int message) {
        i(context, getString(message));
    }

    public static void e(Context context, @StringRes int message) {
        e(context, getString(message));
    }

    public static void w(Context context, @StringRes int message) {
        w(context, getString(message));
    }

    public static void ok(Context context, @StringRes int message) {
        ok(context, getString(message));
    }

    public static void i_long(Context context, @StringRes int message) {
        i_long(context, getString(message));
    }

    public static void e_long(Context context, @StringRes int message) {
        e_long(context, getString(message));
    }

    public static void w_long(Context context, @StringRes int message) {
        w_long(context, getString(message));
    }

    public static void ok_long(Context context, @StringRes int message) {
        ok_long(context, getString(message));
    }

    public static void show(String msg) {
        show(BaseApplication.getContext(), msg);
    }

    public static void show(@StringRes int rid) {
        show(BaseApplication.getContext(), rid);
    }

    public static void showLong(@StringRes int rid) {
        showLong(BaseApplication.getContext(), rid);
    }

    public static void i(String message) {
        i(BaseApplication.getContext(), message);
    }

    public static void e(String message) {
        e(BaseApplication.getContext(), message);
    }

    public static void w(String message) {
        w(BaseApplication.getContext(), message);
    }

    public static void ok(String message) {
        ok(BaseApplication.getContext(), message);
    }

    public static void i_long(String message) {
        i_long(BaseApplication.getContext(), message);

    }

    public static void e_long(String message) {
        e_long(BaseApplication.getContext(), message);
    }

    public static void w_long(String message) {
        w_long(BaseApplication.getContext(), message);
    }

    public static void ok_long(String message) {
        ok_long(BaseApplication.getContext(), message);
    }

    public static void i(@StringRes int message) {
        i(BaseApplication.getContext(), getString(message));
    }

    public static void e(@StringRes int message) {
        e(BaseApplication.getContext(), getString(message));
    }

    public static void w(@StringRes int message) {
        w(BaseApplication.getContext(), getString(message));
    }

    public static void ok(@StringRes int message) {
        ok(BaseApplication.getContext(), getString(message));
    }

    public static void i_long(@StringRes int message) {
        i_long(BaseApplication.getContext(), getString(message));
    }

    public static void e_long(@StringRes int message) {
        e_long(BaseApplication.getContext(), getString(message));
    }

    public static void w_long(@StringRes int message) {
        w_long(BaseApplication.getContext(), getString(message));
    }

    public static void ok_long(@StringRes int message) {
        ok_long(BaseApplication.getContext(), getString(message));
    }
}
