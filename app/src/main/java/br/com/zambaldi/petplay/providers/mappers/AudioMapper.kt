package br.com.zambaldi.petplay.providers.mappers

import br.com.zambaldi.petplay.models.Audio
import br.com.zambaldi.petplay.providers.DataMapper
import br.com.zambaldi.petplay.providers.datasource.local.entity.AudioDomain

object AudioMapper : DataMapper<List<AudioDomain>, List<Audio>>() {
    override fun toDomain(data: List<AudioDomain>): List<Audio> {
        return data.let {
            it.map { audio ->
                Audio(
                    id = audio.id,
                    name = audio.name,
                    path = audio.path
                )
            }
        }
    }
}