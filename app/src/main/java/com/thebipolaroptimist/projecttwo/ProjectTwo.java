package com.thebipolaroptimist.projecttwo;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ProjectTwo extends Application {
    public static final int SCHEMA_VERSION = 1;
    @Override
    public void onCreate ()
    {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("project_two.realm")
                .schemaVersion(SCHEMA_VERSION)
                //.migration(new Migration())
                .build();

        Realm.deleteRealm(configuration);

        Realm.setDefaultConfiguration(configuration);
    }
}
