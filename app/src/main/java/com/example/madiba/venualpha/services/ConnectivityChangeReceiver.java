package com.example.madiba.venualpha.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.madiba.venualpha.Actions.ActionNetwork;
import com.example.madiba.venualpha.util.NetUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Madiba on 11/22/2016.
 */
 public class ConnectivityChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (NetUtils.hasInternetConnection(context)) {
            EventBus.getDefault().post(new ActionNetwork(true));
        } else {
            EventBus.getDefault().post(new ActionNetwork(false));
            }
    }
}