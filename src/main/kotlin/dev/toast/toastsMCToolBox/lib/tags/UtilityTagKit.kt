package dev.toast.toastsMCToolBox.lib.tags

import com.google.gson.Gson
import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.randomBitString
import dev.toast.toastsMCToolBox.lib.rpx.skills.RPSkill

object UtilityTagKit {

    class AlreadyTaggedException(message: String) : Exception(message)
    private val taggedSkills: MutableMap<String, RPSkill> = mutableMapOf()

    class SkillTagger(val skill: RPSkill) {

        fun tag() {
            val isTagged = skill.getFromStorage("tag")
            if (isTagged != null) throw AlreadyTaggedException("Skill is already tagged")
            else {
                val tagValue = randomBitString(18)
                skill.addToStorage("tag", tagValue)
                taggedSkills[tagValue] = skill
            }
        }

        fun untag() {
            val isTagged = skill.getFromStorage("tag")
            if (isTagged == null) throw AlreadyTaggedException("Skill is not tagged")
            else {
                skill.removeFromStorage("tag")
                taggedSkills.remove(isTagged)
            }
        }

        companion object {
        }
    }

    fun onShutdown() {
        val taggedSkillsFile = FileKit.getFile("Skills.json", "${ToolBox.taggedStoragePath}/Skills/")
        taggedSkillsFile.overwrite(Gson().toJson(taggedSkills))
    }

}