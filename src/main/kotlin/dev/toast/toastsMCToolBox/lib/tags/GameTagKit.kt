package dev.toast.toastsMCToolBox.lib.tags

import dev.toast.toastsMCToolBox.lib.ToolBox
import dev.toast.toastsMCToolBox.lib.files.FileKit
import dev.toast.toastsMCToolBox.lib.randomBitString
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

object GameTagKit {

    class BlockAlreadyTaggedException(message: String) : Exception(message)

    data class Tag(val prefix: String, val suffix: String) {
        override fun toString(): String {
            return "$prefix-$suffix"
        }

        companion object {
            fun fromString(string: String): Tag {
                val split = string.split("-")
                return Tag(split[0], split[1])
            }
        }
    }
    private data class TagLocation(val x: Double, val y: Double, val z: Double) {
        override fun toString(): String {
            return "$x,$y,$z"
        }
    }

    enum class TagPrefixes(val prefix: String) {
        BLOCK("8882bC7L"),
        ITEM("u01a24bs"),
        PLAYER("Ha8e8a7L"),

        ;
    }

    /**
     * Tags a block with a random 12 bit string + a 8 bit prefix
     * @param block The block to tag
     * @throws BlockAlreadyTaggedException if the block has already been tagged
     * @return The tag
     */
//    fun tag(block: Block): Tag {
//        val prefix = TagPrefixes.BLOCK.prefix
//        val suffix = randomBitString(12)
//
//        val tag = Tag(prefix, suffix)
//        val location = TagLocation(block.location.x, block.location.y, block.location.z)
//
//        if (recentlyTaggedBlocks.containsKey(tag)) throw BlockAlreadyTaggedException("Block already tagged")
//
//
//        ToolBox.getTagBlockStorage().appendLine("$tag~$location")
//        recentlyTaggedBlocks[tag] = location
//        return tag
//    }

    fun tag(item: ItemStack): Tag {
        val prefix = TagPrefixes.ITEM.prefix
        val suffix = randomBitString(32)

        val tag = Tag(prefix, suffix)

        val meta = item.itemMeta ?: throw IllegalStateException("Item has no item meta")
        meta.persistentDataContainer.set(ToolBox.TAG_KEY, PersistentDataType.STRING, tag.toString())
        item.itemMeta = meta
        return tag
    }

    fun tag(player: Player): Tag {
        val prefix = TagPrefixes.PLAYER.prefix
        val suffix = randomBitString(12)

        val tag = Tag(prefix, suffix)

        val meta = player.persistentDataContainer
        meta.set(ToolBox.TAG_KEY, PersistentDataType.STRING, tag.toString())
        return tag
    }

    fun isTagged(item: ItemStack): Boolean {
        val itemMeta = item.itemMeta ?: return false
        val pdc = itemMeta.persistentDataContainer
        return pdc.has(ToolBox.TAG_KEY, PersistentDataType.STRING)
    }

    fun getTag(item: ItemStack): Tag? {
        val itemMeta = item.itemMeta ?: return null
        val pdc = itemMeta.persistentDataContainer
        val tagString = pdc.get(ToolBox.TAG_KEY, PersistentDataType.STRING) ?: return null
        return Tag.fromString(tagString)
    }


    /**
     * Parses the block tags from a file sequentially.
     * This method breaks the file into chunks, and parses each chunk sequentially.
     * After you process a chunk, and request the next chunk, it will be parsed.
     * @param file The file to parse.
     * @param numChunks The number of chunks to break the file into. Default is 4.
     * @return A sequence of lists of pairs<tag, location>. Each list represents a chunk of the file.
     */
    fun parseBlockTagsSequentially(file: FileKit.FileStorage, numChunks: Int = 4): Sequence<List<Pair<String, String>>> {
        val lines = file.readLines()
        val chunks = chunkFile(lines, numChunks)

        return sequence {
            for (chunk in chunks) {
                val tags = mutableListOf<Pair<String, String>>()
                for (line in chunk) {
                    val split = line.split("-")
                    if (split.size >= 2) {
                        tags.add(Pair(split[0], split[1]))
                    }
                }
                yield(tags)
            }
        }
    }

    private fun chunkFile(lines: List<String>, numChunks: Int): List<List<String>> {
        val totalLines = lines.size
        val chunkSize = totalLines / numChunks
        val chunks = mutableListOf<List<String>>()

        for (i in 0 until numChunks) {
            val start = i * chunkSize
            val end = if (i == numChunks - 1) totalLines else (i + 1) * chunkSize
            chunks.add(lines.subList(start, end))
        }

        return chunks
    }

}


