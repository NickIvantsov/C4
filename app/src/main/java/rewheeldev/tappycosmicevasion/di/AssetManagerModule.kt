package rewheeldev.tappycosmicevasion.di

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AssetManagerModule {
    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager {
        return context.assets
    }
}