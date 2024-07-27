package dev.toast.toastsMCToolBox.lib.files
import java.io.File
import java.io.IOException

object FileKit {

    class FileStorage(private val name: String, private val parentDirectory: String) {

        private val file = File("$parentDirectory$name")
        private val extension: String = file.extension

//        init {
//            println("File storage name: $name")
//            println("File storage parent directory: $parentDirectory")
//            try {
//                if (!file.exists()) {
//                    file.parentFile.mkdirs()
//                    file.createNewFile()
//                    println("Created new file: ${file.absolutePath}")
//                } else {
//                    println("File already exists: ${file.absolutePath}")
//                }
//            } catch (e: IOException) {
//                println("Error creating file: ${e.message}")
//            }
//        }

        fun doIExist(): Boolean {
            return file.exists()
        }

        fun appendLine(content: String) {
            if (!file.exists()) throw IllegalStateException("File does not exist")
            if (!file.canWrite()) throw IllegalStateException("File is not writable")
            try {
                file.appendText("$content\n")
                println("Appended to file: $content")
            } catch (e: IOException) {
                println("Error appending to file: ${e.message}")
            }
        }

        fun read(): String {
            if (!file.exists()) throw IllegalStateException("File does not exist")
            return try {
                file.readText()
            } catch (e: IOException) {
                println("Error reading file: ${e.message}")
                ""
            }
        }

        fun deleteFile() {
            if (!file.exists()) throw IllegalStateException("File does not exist")
            try {
                file.delete()
                println("File deleted: ${file.absolutePath}")
            } catch (e: IOException) {
                println("Error deleting file: ${e.message}")
            }
        }

        fun overwrite(content: String) {
            if (!file.exists()) throw IllegalStateException("File does not exist")
            try {
                file.writeText(content)
                println("Overwritten file with content: $content")
            } catch (e: IOException) {
                println("Error overwriting file: ${e.message}")
            }
        }

        fun readLines(): List<String> {
            if (!file.exists()) throw IllegalStateException("File does not exist")
            return try {
                file.readLines()
            } catch (e: IOException) {
                println("Error reading file lines: ${e.message}")
                emptyList()
            }
        }

        fun getExtension(): String {
            return extension
        }
    }
}
