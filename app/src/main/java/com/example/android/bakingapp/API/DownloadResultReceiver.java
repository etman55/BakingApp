package com.example.android.bakingapp.API;

/**
 * Created by Etman on 6/16/2017.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class DownloadResultReceiver extends ResultReceiver {
    private Receiver mReceiver;

    public DownloadResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}

