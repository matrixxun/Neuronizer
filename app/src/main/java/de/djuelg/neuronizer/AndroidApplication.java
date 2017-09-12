package de.djuelg.neuronizer;

import android.app.Application;

import de.djuelg.neuronizer.storage.migration.RealmMigrator;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import static de.djuelg.neuronizer.storage.RepositoryManager.SCHEMA_VERSION;

public class AndroidApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // initiate Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(SCHEMA_VERSION)
                .migration(new RealmMigrator())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
