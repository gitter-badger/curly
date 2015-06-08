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

import groovy.transform.PackageScope

/**
 * @author Joao Pedro Evangelista
 */
@PackageScope
final class OwningAssert {

    private OwningAssert() {}

    public static boolean matchOwners(Paper paper, String owner) {
        paper.owner == owner
    }

    public static boolean wasModified(Paper saved, String owner, Paper paper) {
        saved.owner == owner && saved.item == paper.item
    }
}
