package com.permissionnanny;

import android.support.annotation.StringRes;
import com.permissionnanny.content.ContentOperation;
import com.permissionnanny.lib.request.RequestParams;
import com.permissionnanny.lib.request.content.ContentRequest;
import com.permissionnanny.operation.SimpleOperation;

/**
 *
 */
public class Operation {
    public static Operation getOperation(RequestParams request) {
        switch (request.opCode) {
        case ContentRequest.SELECT:
        case ContentRequest.INSERT:
        case ContentRequest.UPDATE:
        case ContentRequest.DELETE:
            return ContentOperation.getOperation(request);
        default:
            return SimpleOperation.getOperation(request);
        }
    }

    @StringRes public final int mDialogTitle;
    public final int mMinSdk;

    public Operation(int dialogTitle, int minSdk) {
        mDialogTitle = dialogTitle;
        mMinSdk = minSdk;
    }
}
