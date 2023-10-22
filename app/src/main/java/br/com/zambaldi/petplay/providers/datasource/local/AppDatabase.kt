package br.com.zambaldi.petplay.providers.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.zambaldi.petplay.providers.datasource.local.dao.AudioDAO
import br.com.zambaldi.petplay.providers.datasource.local.dao.AudiosGroupDAO
import br.com.zambaldi.petplay.providers.datasource.local.dao.GroupDAO
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudiosGroupDomain
import br.com.zambaldi.petplay.providers.datasource.local.entity.GroupDomain

@Database(entities = [
    AudioDomain::class,
    GroupDomain::class,
    AudiosGroupDomain::class
], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun audioDAO(): AudioDAO
    abstract fun groupDAO(): GroupDAO
    abstract fun audiosGroupDAO(): AudiosGroupDAO

}