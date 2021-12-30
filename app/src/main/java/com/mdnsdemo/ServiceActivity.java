/**
 * Copyright (C) 2021 Terminator712
 */

package com.mdnsdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.druk.rx2dnssd.BonjourService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URL;

public class ServiceActivity extends AppCompatActivity implements ServiceDetailFragment.ServiceDetailListener {

    private static final String SERVICE = "mService";
    private static final String REGISTERED = "registered";

    private TextView mServiceName;
    private TextView mRegType;
    private TextView mDomain;
    private TextView mLastTimestamp;

    public static void startActivity(Context context, BonjourService service) {
        context.startActivity(new Intent(context, ServiceActivity.class).
                putExtra(ServiceActivity.SERVICE, service));
    }

    public static Intent startActivity(Context context, BonjourService service, boolean isRegistered) {
        return new Intent(context, ServiceActivity.class).putExtra(ServiceActivity.SERVICE, service).putExtra(REGISTERED, isRegistered);
    }

    public static BonjourService parseResult(Intent intent) {
        return intent.getParcelableExtra(SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
//        setSupportActionBar(findViewById(R.id.toolbar));
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        FloatingActionButton fab = findViewById(R.id.fab);
        mServiceName = findViewById(R.id.service_name);
        mRegType = findViewById(R.id.reg_type);
        mDomain = findViewById(R.id.domain);
        mLastTimestamp = findViewById(R.id.last_timestamp);

        ServiceDetailFragment serviceDetailFragment;
        boolean isRegistered = getIntent().getBooleanExtra(REGISTERED, false);

        if (savedInstanceState == null) {
            BonjourService service = getIntent().getParcelableExtra(SERVICE);
            serviceDetailFragment = ServiceDetailFragment.newInstance(service);
            getSupportFragmentManager().beginTransaction().replace(R.id.content, serviceDetailFragment).commit();
        } else {
            serviceDetailFragment = (ServiceDetailFragment) getSupportFragmentManager().findFragmentById(R.id.content);
        }

        if (isRegistered && fab != null) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(serviceDetailFragment);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onServiceUpdated(BonjourService service) {
        mServiceName.setText(service.getServiceName());
        mDomain.setText(getString(R.string.domain, service.getDomain()));
        mRegType.setText(getString(R.string.reg_type, service.getRegType()));
        mLastTimestamp.setText(getString(R.string.last_update, Utils.formatTime(System.currentTimeMillis())));
    }

    @Override
    public void onServiceStopped(BonjourService service) {
        setResult(Activity.RESULT_OK, new Intent().putExtra(SERVICE, service));
        finish();
    }

    @Override
    public void onHttpServerFound(URL url) {
        FloatingActionButton fab = findViewById(R.id.fab);
        if (fab.getVisibility() != View.VISIBLE) {
            fab.setVisibility(View.VISIBLE);
            fab.setScaleX(0);
            fab.setScaleY(0);
            fab.animate()
                    .alpha(1.0f)
                    .scaleX(1)
                    .scaleY(1)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
        }

        fab.setOnClickListener(view -> {
//            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//            builder.setToolbarColor(ContextCompat.getColor(this, R.color.primary));
//            CustomTabsIntent customTabsIntent = builder.build();
//            try {
//                customTabsIntent.launchUrl(this, Uri.parse(url.toString()));
//            }
//            catch (Throwable e) {
//                Toast.makeText(this, "Can't find browser", Toast.LENGTH_SHORT).show();
//            }
        });
    }

}
