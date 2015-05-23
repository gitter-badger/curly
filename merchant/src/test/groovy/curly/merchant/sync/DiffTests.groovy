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
package curly.merchant.sync

import curly.merchant.ExportedOctoRepository
import curly.merchant.OctoRepository
import org.junit.Before
import org.junit.Test
import org.springframework.util.MultiValueMap

import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertThat
import static org.junit.Assert.assertTrue

/**
 * @author Joao Pedro Evangelista
 */
class DiffTests {
    List<ExportedOctoRepository> repos
    List<ExportedOctoRepository> request
    MultiValueMap<Operation, ExportedOctoRepository> multiMap

    ExportedOctoRepository repo1
    ExportedOctoRepository repo2
    ExportedOctoRepository repo3
    ExportedOctoRepository repo4
    ExportedOctoRepository repo5

    @Before
    public void setUp() throws Exception {

        repo1 = new ExportedOctoRepository(new OctoRepository(id: 123, name: "Spring", description: "spr"))
        repo2 = new ExportedOctoRepository(new OctoRepository(id: 123, name: "Spring-io", description: "spr"))
        repo3 = new ExportedOctoRepository(new OctoRepository(id: 1234, name: "Guava", description: "gua"))
        repo4 = new ExportedOctoRepository(new OctoRepository(id: 12345, name: "Groovy", description: "gro"))
        repo5 = new ExportedOctoRepository(new OctoRepository(id: 123456, name: "Scala", description: "scala"))

        this.repos = new LinkedList(Arrays.asList(repo1, repo4, repo5)).asImmutable()
        this.request = new LinkedList(Arrays.asList(repo2, repo3, repo4)).asImmutable()
    }

    @Test
    public void testDiff() throws Exception {
        this.multiMap = new Diff().invoke(repos, request)
        assertTrue(multiMap.containsKey(Operation.KEEP))
        assertTrue(multiMap.containsKey(Operation.REMOVE))
        assertTrue(multiMap.containsKey(Operation.ADD))
        assertThat(multiMap.get(Operation.ADD), hasSize(2))
        assertThat(multiMap.get(Operation.REMOVE), hasSize(2))
        assertThat(multiMap.get(Operation.KEEP), hasSize(1))
    }

}
