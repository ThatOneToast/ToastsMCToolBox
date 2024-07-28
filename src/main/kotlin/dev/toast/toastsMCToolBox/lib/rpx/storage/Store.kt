package dev.toast.toastsMCToolBox.lib.rpx.storage

import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object Store {

    @Serializable(with = SkillsUnitSerializer::class)
    data class SkillsUnit(val skills: MutableSet<RPSkill>) {
        fun serialize(): String {
            return Json.encodeToString(serializer(), this)
        }

        companion object {
            fun deserialize(json: String): SkillsUnit {
                return Json.decodeFromString(serializer(), json)
            }
        }
    }


}
