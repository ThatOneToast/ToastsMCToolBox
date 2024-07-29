package dev.toast.toastsMCToolBox.lib.files
import java.io.File
import java.io.IOException

object FileKit {

    class FileDoesntExistException(message: String) : Exception(message)
    class FileCantReadException(message: String) : Exception(message)
    class FileCantWriteException(message: String) : Exception(message)

    /**
     * Returns a classic file with exists, and can read checks.
     * @param name The name of the file
     * @param parentDirectory The parent directory of the file
     * @return The file
     * @throws FileDoesntExistException if the file doesn't exist
     * @throws FileCantReadException if the file can't be read
     */
    fun getNormyFile(name: String, parentDirectory: String): File {
        val file = File("$parentDirectory$name")
        if (!file.exists()) throw FileDoesntExistException("File does not exist")
        if (!file.canRead()) throw FileCantReadException("File can't be read")
        return file
    }

    /**
     * Returns the file in FileStorage format
     * @param name The name of the file
     * @param parentDirectory The parent directory of the file
     * @return The FileStorage
     */
    fun getFile(name: String, parentDirectory: String): FileStorage = FileStorage(name, parentDirectory)

    class FileStorage(private val name: String, private val parentDirectory: String) {

        private val file = File("$parentDirectory$name")
        private val extension: String = file.extension

        init {
            println("File storage name: $name")
            println("File storage parent directory: $parentDirectory")
            try {
                if (!file.exists()) {
                    file.parentFile.mkdirs()
                    file.createNewFile()
                    println("Created new file: ${file.absolutePath}")
                } else {
                    println("File already exists: ${file.absolutePath}")
                }
            } catch (e: IOException) {
                println("Error creating file: ${e.message}")
            }
            if (!file.canRead()) throw FileCantReadException("File can't be read")
            if (!file.canWrite()) throw FileCantWriteException("File can't be written")
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
