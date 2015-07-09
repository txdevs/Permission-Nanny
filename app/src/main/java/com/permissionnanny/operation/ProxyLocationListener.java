package com.permissionnanny.operation;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import com.permissionnanny.ProxyListener;
import com.permissionnanny.ProxyService;
import com.permissionnanny.lib.Nanny;
import com.permissionnanny.lib.request.location.LocationEvent;

public class ProxyLocationListener extends ProxyListener implements LocationListener {

    public ProxyLocationListener(ProxyService service, String clientAddr) {
        super(service, clientAddr);
    }

    @Override
    protected void unregister(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Bundle entity = new Bundle();
        entity.putString(LocationEvent.TYPE, LocationEvent.ON_LOCATION_CHANGED);
        entity.putParcelable(LocationEvent.LOCATION, location);

        sendBroadcast(newResponseIntent(Nanny.LOCATION_SERVICE, entity));
    }

    @Override
    public void onProviderDisabled(String provider) {
        Bundle entity = new Bundle();
        entity.putString(LocationEvent.TYPE, LocationEvent.ON_PROVIDER_DISABLED);
        entity.putString(LocationEvent.PROVIDER, provider);

        sendBroadcast(newResponseIntent(Nanny.LOCATION_SERVICE, entity));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Bundle entity = new Bundle();
        entity.putString(LocationEvent.TYPE, LocationEvent.ON_PROVIDER_ENABLED);
        entity.putString(LocationEvent.PROVIDER, provider);

        sendBroadcast(newResponseIntent(Nanny.LOCATION_SERVICE, entity));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Bundle entity = new Bundle();
        entity.putString(LocationEvent.TYPE, LocationEvent.ON_STATUS_CHANGED);
        entity.putString(LocationEvent.PROVIDER, provider);
        entity.putInt(LocationEvent.STATUS, status);
        entity.putBundle(LocationEvent.EXTRAS, extras);

        sendBroadcast(newResponseIntent(Nanny.LOCATION_SERVICE, entity));
    }
}
