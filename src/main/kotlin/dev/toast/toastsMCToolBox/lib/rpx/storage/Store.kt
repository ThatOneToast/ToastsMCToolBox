package dev.toast.toastsMCToolBox.lib.rpx.storage

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill

object Store {


    data class SkillsUnit(val skills: MutableSet<RPSkill.SkillValues>) {
        fun serialize(): String {
            val newJson = Gson().toJson(this)
            return newJson
        }

        companion object {
            fun deserialize(json: String): SkillsUnit {
                val skills = Gson().fromJson(json, SkillsUnit::class.java)
                return skills
            }
        }
    }

    data class ItemHandlerValues(val values: MutableMap<String, String>) { // Name:ID
        fun toJson(): String {
            return Gson().toJson(this)
        }

        companion object {
            fun fromJson(json: String): ItemHandlerValues? {
                val gson = Gson()
                return gson.fromJson(json, ItemHandlerValues::class.java)
            }
        }
    }


}
