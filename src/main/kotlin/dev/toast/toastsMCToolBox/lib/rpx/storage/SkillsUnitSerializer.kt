package dev.toast.toastsMCToolBox.lib.rpx.storage

import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkillSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*


object SkillsUnitSerializer : KSerializer<Store.SkillsUnit> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("SkillsUnit") {
        element<List<RPSkill>>("skills")
    }

    override fun serialize(encoder: Encoder, value: Store.SkillsUnit) {
        encoder.encodeStructure(descriptor) {
            encodeSerializableElement(descriptor, 0, ListSerializer(RPSkillSerializer), value.skills.toList())
        }
    }

    override fun deserialize(decoder: Decoder): Store.SkillsUnit {
        lateinit var skills: List<RPSkill>

        decoder.decodeStructure(descriptor) {
            loop@while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> skills = decodeSerializableElement(descriptor, index, ListSerializer(RPSkillSerializer))
                    CompositeDecoder.DECODE_DONE -> break@loop
                    else -> throw SerializationException("Unknown index $index")
                }
            }
        }

        return Store.SkillsUnit(skills.toMutableSet())
    }
}