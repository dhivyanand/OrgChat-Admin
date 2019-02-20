package com.example.system.orgchatadmin.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ApplicationBackgroundService extends Service {
    public ApplicationBackgroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
