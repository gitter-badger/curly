package curly.paperclip.paper

import java.nio.file.{Files, Paths}

/**
 * @author Joao Evangelista
 */
class LocalStorageAccessor extends StorageAccessor {

  val systemHome = System.getenv("HOME")

  override def rawContent(path: String): String = {
    val path = java.nio.file.Paths.get(systemHome, path)
    new String(Files.readAllBytes(path))
  }

  override def write(content: String, artifact: String): Unit = {
    val dir = Files.createDirectory(Paths.get(systemHome, artifact)).toAbsolutePath.toString
    val file = Paths.get(dir, "README.md")
    Files.write(file, content.getBytes)
  }
}

