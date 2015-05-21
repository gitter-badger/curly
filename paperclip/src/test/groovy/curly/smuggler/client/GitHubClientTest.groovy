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
package test.groovy.curly.smuggler.client

import curly.MerchantApplication
import curly.commons.github.OctoUser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import rx.Observable
import rx.schedulers.Schedulers

import javax.inject.Inject

import static curly.commons.reactor.DispatcherFactory.ringBufferDispatcher
import static curly.commons.reactor.DispatcherFactory.workQueueDispatcher
import static org.hamcrest.Matchers.greaterThan
import static org.hamcrest.Matchers.hasSize
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertThat

/**
 * @author Joao Pedro Evangelista
 */
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = MerchantApplication)
class GitHubClientTest {

    @Inject
    GitHubClient gitHubClient

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetRepositories() throws Exception {
        def repositories = gitHubClient.getRepositories(new OctoUser(login: "joaoevangelista"))
        assertNotNull("repositories must be not null", repositories)
        Observable.from(repositories, Schedulers.from(ringBufferDispatcher(GitHubClientTest)))
                .observeOn(Schedulers.from(workQueueDispatcher(GitHubClientTest)))
                .retry(1)
                .filter({ it.statusCode.is2xxSuccessful() })
                .map({ new LinkedList<>(Arrays.asList(it.body)) })
                .subscribe({
            assertNotNull("List of repositories must be not null", it)
            assertThat("List of repositories must be greater than one", it, hasSize(greaterThan(1)))
        })

    }
}
