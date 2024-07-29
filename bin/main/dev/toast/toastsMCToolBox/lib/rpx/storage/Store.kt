package dev.toast.toastsMCToolBox.lib.rpx.storage

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill

object Store {


    data class SkillsUnit(val skills: MutableSet<RPSkill.SkillValues>) {
        fun serialize(): String {
            val listToReturn: List<String> = skills.map { it.serialize() }
            val newJson = Gson().toJson(listToReturn)
            return newJson
        }

        companion object {
            fun deserialize(json: String): SkillsUnit {
                val skillValues = Gson().fromJson(json, Array<RPSkill.SkillValues>::class.java)
                val skills = skillValues.toMutableSet()
                return SkillsUnit(skills)
            }
        }
    }


}
