package com.permissionnanny.lib.request;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import com.permissionnanny.lib.BundleEvent;
import com.permissionnanny.lib.BundleListener;
import com.permissionnanny.lib.Event;
import com.permissionnanny.lib.Nanny;
import com.permissionnanny.lib.PermissionReceiver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.SecureRandom;

/**
 *
 */
public abstract class PermissionRequest {

    public static final String REQUEST_TYPE = "requestType";
    public static final String REQUEST_PARAMS = "requestParams";
    public static final String REQUEST_REASON = "requestReason";
    public static final String CLIENT_PACKAGE = "clientPackage";

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({CURSOR_REQUEST, LOCATION_REQUEST, SMS_REQUEST, TELEPHONY_REQUEST, WIFI_REQUEST})
    public @interface RequestType {}

    public static final int CURSOR_REQUEST = 1;
    public static final int LOCATION_REQUEST = 100;
    public static final int SMS_REQUEST = 200;
    public static final int TELEPHONY_REQUEST = 300;
    public static final int WIFI_REQUEST = 400;

    /** An empty intent. This constant must not be modified. */
    private static final Intent EMPTY_INTENT = new Intent();

    protected PermissionReceiver mReceiver;
    protected RequestParams mParams;

    public PermissionRequest(RequestParams body) {
        mParams = body;
    }

    @RequestType
    public abstract int getRequestType();

    public Intent decorateIntent(Intent intent, Context context, String reason, @Nullable String clientFilter) {
        Bundle entity = new Bundle();
        entity.putParcelable(CLIENT_PACKAGE, PendingIntent.getBroadcast(context, 0, EMPTY_INTENT, 0));
        entity.putInt(REQUEST_TYPE, getRequestType());
        entity.putParcelable(REQUEST_PARAMS, mParams);
        entity.putString(REQUEST_REASON, reason);

        if (clientFilter != null) {
            intent.putExtra(Nanny.CLIENT_ADDRESS, clientFilter);
        }
        return intent.putExtra(Nanny.PROTOCOL_VERSION, Nanny.PPP_1_0)
                .putExtra(Nanny.CONTENT_TYPE, Bundle.class.getCanonicalName())
                .putExtra(Nanny.CONTENT_ENCODING, Nanny.ENCODING_BUNDLE)
                .putExtra(Nanny.ENTITY_BODY, entity);
    }

    public Intent newBroadcastIntent(Context context, String reason, @Nullable String clientFilter) {
        Intent intent = new Intent()
                .setClassName(Nanny.SERVER_APP_ID, Nanny.CLIENT_REQUEST_RECEIVER)
                .setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        return decorateIntent(intent, context, reason, clientFilter);
    }

    public Intent newBindServiceIntent() {
        return new Intent().setClassName("com.sdchang.permissionpolice",
                "com.sdchang.permissionpolice.ExternalRequestService");
    }

    public PermissionRequest listener(BundleListener listener) {
        return addFilter(new BundleEvent(listener));
    }

    protected PermissionRequest addFilter(Event event) {
        if (mReceiver == null) {
            mReceiver = new PermissionReceiver();
        }
        mReceiver.addFilter(event);
        return this;
    }

    public PermissionRequest startRequest(Context context, String reason) {
        String clientId = null;
        if (mReceiver != null) {
            clientId = Long.toString(new SecureRandom().nextLong());
            context.registerReceiver(mReceiver, new IntentFilter(clientId));
        }
        context.sendBroadcast(newBroadcastIntent(context, reason, clientId));
        return this;
    }
}
