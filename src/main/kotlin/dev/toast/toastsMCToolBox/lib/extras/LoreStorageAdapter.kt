package dev.toast.toastsMCToolBox.lib.extras

import dev.toast.toastsMCToolBox.lib.lore.LoreKit
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

class LoreStorageAdapter : PersistentDataType<ByteArray, LoreKit.LoreStorage> {

    override fun getPrimitiveType(): Class<ByteArray> {
        return ByteArray::class.java
    }

    override fun getComplexType(): Class<LoreKit.LoreStorage> {
        return LoreKit.LoreStorage::class.java
    }

    override fun toPrimitive(complex: LoreKit.LoreStorage, context: PersistentDataAdapterContext): ByteArray {
        return complex.toByteArray()
    }

    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): LoreKit.LoreStorage {
        return LoreKit.LoreStorage.fromByteArray(primitive)
    }

}