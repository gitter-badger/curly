/*
 *        Copyright 2015 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package curly.paperclip.paper

import java.nio.file.{Files, Paths}

/**
 * Reader and writer for local storage only
 *
 * @author Joao Evangelista
 */
class LocalStorageAccessor extends StorageAccessor {

  val systemHome = System.getenv("HOMEPATH")

  override def rawContent(path: String): String = {
    val fullPath = java.nio.file.Paths.get(systemHome, path)
    new String(Files.readAllBytes(fullPath))
  }

  override def write(content: String, artifact: String): String = {
    val dir = Files.createDirectory(Paths.get(systemHome, artifact)).toAbsolutePath.toString
    val file = Paths.get(dir, "PAPER.md")
    val savedPath = Files.write(file, content.getBytes)
    savedPath.toAbsolutePath.toString
  }
}

