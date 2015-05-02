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
