package curly.paperclip.paper

trait StorageAccessor {

  def rawContent(path: String): String

  def write(content: String, artifact: String): Unit
}
