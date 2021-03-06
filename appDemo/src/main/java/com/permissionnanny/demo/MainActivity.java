package com.permissionnanny.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.permissionnanny.demo.account.AccountRequestFactory;
import com.permissionnanny.demo.deeplink.DemoDeepLinkActivity;
import com.permissionnanny.demo.location.LocationRequestFactory;
import com.permissionnanny.demo.telephony.TelephonyRequestFactory;
import com.permissionnanny.demo.wifi.WifiRequestFactory;

public class MainActivity extends BaseActivity {

    public static String FACTORY_ID = "factoryId";

    public static String[] mLabels = new String[]{
            "Permission Manifest Demo",
            "DeepLink Demo",
            "ContentRequestDemo",
            "LocationRequestDemo",
            "TelephonyRequestDemo",
            "WifiRequestDemo",
            "AccountRequestDemo"
    };
    public static SimpleRequestFactory[] mFactories = new SimpleRequestFactory[]{
            null,
            null,
            null,
            new LocationRequestFactory(),
            new TelephonyRequestFactory(),
            new WifiRequestFactory(),
            new AccountRequestFactory(),
    };

    @Bind(R.id.rv) RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new MyAdapter());
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public int getItemCount() {
            return mLabels.length;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.main_listitem, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.tv1.setText(mLabels[position]);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, DemoPermissionManifestActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, DemoDeepLinkActivity.class));
                        break;
                    default:
                        startActivity(new Intent(MainActivity.this, DemoActivity.class)
                                .putExtra(FACTORY_ID, position));
                    }
                }
            });
        }
    }

    class MyViewHolder extends ViewHolder {
        @Bind(R.id.tv1) TextView tv1;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
