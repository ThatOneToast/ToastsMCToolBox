package dev.toast.toastsMCToolBox.lib.rpx.skills

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*

object RPSkillSerializer : KSerializer<RPSkill> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("RPSkill") {
        element<String>("name")
        element<String>("description")
        element<Int>("levelRequirement")
        element<Int>("cooldownTime")
        element<Int>("maxLevel")
        element<Int>("currentLevel")
        element<String>("listener")
        element<String>("identifier")
    }

    override fun serialize(encoder: Encoder, value: RPSkill) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.getName())
            encodeStringElement(descriptor, 1, value.getDescription())
            encodeIntElement(descriptor, 2, value.getLevelRequirement())
            encodeIntElement(descriptor, 3, value.getCooldownTime())
            encodeIntElement(descriptor, 4, value.getMaxLevel())
            encodeIntElement(descriptor, 5, value.getCurrentLevel())
            encodeStringElement(descriptor, 6, value.listener)
            encodeStringElement(descriptor, 7, value.identifier)
        }
    }

    override fun deserialize(decoder: Decoder): RPSkill {
        lateinit var name: String
        lateinit var description: String
        var levelRequirement: Int = 0
        var cooldownTime: Int = 0
        var maxLevel: Int = 0
        var currentLevel: Int = 0
        lateinit var listener: String
        lateinit var identifier: String

        decoder.decodeStructure(descriptor) {
            loop@while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> name = decodeStringElement(descriptor, index)
                    1 -> description = decodeStringElement(descriptor, index)
                    2 -> levelRequirement = decodeIntElement(descriptor, index)
                    3 -> cooldownTime = decodeIntElement(descriptor, index)
                    4 -> maxLevel = decodeIntElement(descriptor, index)
                    5 -> currentLevel = decodeIntElement(descriptor, index)
                    6 -> listener = decodeStringElement(descriptor, index)
                    7 -> identifier = decodeStringElement(descriptor, index)
                    CompositeDecoder.DECODE_DONE -> break@loop
                    else -> throw SerializationException("Unknown index $index")
                }
            }
        }

        return object : RPSkill() {
            override fun getName(): String = name
            override fun getDescription(): String = description
            override fun getLevelRequirement(): Int = levelRequirement
            override fun getCooldownTime(): Int = cooldownTime
            override fun getMaxLevel(): Int = maxLevel
            override fun getCurrentLevel(): Int = currentLevel
            override val listener: String = listener
            init {
                this.identifier = identifier
            }
        }
    }
}