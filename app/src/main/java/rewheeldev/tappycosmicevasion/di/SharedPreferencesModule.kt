package rewheeldev.tappycosmicevasion.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

const val SAVED_TEXT = "nick_name"

@Module
class SharedPreferencesModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SAVED_TEXT, AppCompatActivity.MODE_PRIVATE)
    }
}