package com.example.krokzakrokom.data.di;

import android.content.Context;
import com.example.krokzakrokom.data.preferences.AppSharedPreferences;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public AppSharedPreferences provideCustomSharedPreferences(@ApplicationContext Context context) {
        return new AppSharedPreferences(context);
    }
}
