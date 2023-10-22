package br.com.zambaldi.petplay

import android.app.Application
import br.com.zambaldi.petplay.di.DependencyModule
import com.example.myapplicationtest.utils.AppPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PetPlayApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@PetPlayApp)
            modules(
                DependencyModule.appModules
            )
        }

        //Shared Preferences
        AppPreferences.init("pet_play", applicationContext)

    }

}