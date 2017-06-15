package com.linsh.lshapp.model.action;

import com.linsh.lshapp.model.throwabes.CustomThrowable;
import com.linsh.lshapp.tools.HttpErrorCatcher;
import com.linsh.lshutils.utils.Basic.LshToastUtils;

import rx.functions.Action1;

/**
 * Created by Senh Linsh on 17/4/28.
 */

public class DefaultThrowableAction implements Action1<Throwable> {

    @Override
    public void call(Throwable throwable) {
        showThrowableMsg(throwable);
    }

    public static void showThrowableMsg(Throwable throwable) {
        throwable.printStackTrace();
        if (HttpErrorCatcher.isHttpError(throwable)) {
            LshToastUtils.showToast(HttpErrorCatcher.dispatchError(throwable));
        } else if (throwable instanceof CustomThrowable) {
            LshToastUtils.showToast(throwable.getMessage());
        } else {
            LshToastUtils.showToast(throwable.getMessage());
        }
    }
}
