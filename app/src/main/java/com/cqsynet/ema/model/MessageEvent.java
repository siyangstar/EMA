package com.cqsynet.ema.model;

import android.os.Bundle;

public class MessageEvent {

    private Bundle mBundle;

    public MessageEvent(Bundle bundle) {
        mBundle = bundle;
    }

    public Bundle getMessage() {
        return mBundle;
    }

    public void setMessage(Bundle bundle) {
        mBundle = bundle;
    }
}