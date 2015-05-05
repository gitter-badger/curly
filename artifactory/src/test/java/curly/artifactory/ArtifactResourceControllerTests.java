package curly.artifactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import curly.artifact.Artifact;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Joao Pedro Evangelista
 */
public class ArtifactResourceControllerTests extends SpringBootTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Artifact artifact;

    @Before
    public void setUp() throws Exception {
        this.artifact = createArtifact(mongoTemplate);
        this.mockMvc = webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .alwaysExpect(status().is2xxSuccessful())
                .build();
    }

    @Test
    public void testPostPutAndSave() throws Exception {
        mockMvc.perform(
                asyncDispatch(
                        mockMvc.perform(post("/arts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(artifact))
                                .principal(octoUser()))
                                .andExpect(request().asyncStarted())
                                .andExpect(request().asyncResult(ResponseEntity.ok().build()))
                                .andReturn()));

    }

    @Test
    public void testArtifactResource() throws Exception {
        String id = artifact.getId();
        mockMvc.perform(
                asyncDispatch(
                        mockMvc.perform(get("/arts/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(request().asyncStarted())
                                .andExpect(request().asyncResult(ResponseEntity.ok(artifact)))
                                .andReturn()));
    }


}
