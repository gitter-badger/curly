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
package curly.commons.security.negotiation;

import curly.commons.github.OctoUser;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Joao Pedro Evangelista
 */
public class OperationsTests {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testOnSaveException() throws Exception {
        thrown.expect(OperationViolationException.class);
        SampleImpl sample = new SampleImpl();
        SampleResource sampleResource = new SampleResource();
        sampleResource.setOwner("1234");
        OctoUser user = new OctoUser(4321L);
        sample.onSave(sampleResource, user);
    }

    @Test
    public void testOnSave() throws Exception {
        SampleImpl sample = new SampleImpl();
        SampleResource sampleResource = new SampleResource();
        OctoUser user = new OctoUser(1234L);
        assertTrue(sampleResource.getOwner() == null);
        sample.onSave(sampleResource, user);
        assertEquals("Owner must be equal than user id", sampleResource.getOwner(), user.getId().toString());
    }
}
