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

import curly.commons.github.OctoUser
import rx.Observable

/**
 * @author Joao Pedro Evangelista
 */
interface PaperCommand {

    Observable<Optional<Paper>> getByItem(String item)

    Observable<Optional<Paper>> getByOwner(String item, Optional<OctoUser> user)

    void delete(Paper paper, Optional<OctoUser> user)

    Optional<Paper> save(Paper paper, Optional<OctoUser> user)
}
