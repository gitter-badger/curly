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
package curly.paperclip

import curly.commons.github.OctoUser
import curly.paperclip.paper.Paper
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.mock.http.MockHttpOutputMessage
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.nio.charset.Charset

import static org.bson.types.ObjectId.get

/**
 * @author Joao Pedro Evangelista
 */
@RunWith(SpringJUnit4ClassRunner.class)
@OAuth2ContextConfiguration
@SpringApplicationConfiguration(classes = [PaperclipApplication])
@WebIntegrationTest
abstract class SpringBootTestAdapter {


    public static String json(Object o, HttpMessageConverter<Object> httpMessageConverter) {
        MockHttpOutputMessage message = new MockHttpOutputMessage();
        try {
            httpMessageConverter.write(o, jsonMediaType(), message);
        } catch (IOException ignore) {
        }
        return message.getBodyAsString();
    }

    public static MediaType jsonMediaType() {
        return new MediaType(MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf-8"));
    }

    public static OctoUser octoUser() {
        return new OctoUser("evangelistajoaop@gmail.com", true, 6969, 0, 1, 10, "http://example.com/example.jpg", "",
                "", "", "", "", "joaoevangelista", "Joao Pedro Evangelista", OctoUser.TYPE_USER, "http://example.com");
    }


    static Paper createPaper(MongoTemplate mongoTemplate) {
        def paper = new Paper(item: get().toHexString())
        paper.owner = "6969"
        paper.content = "#{urly\n" +
                "\n" +
                "![Logo](https://raw.githubusercontent.com/joaoevangelista/curly/master/src/logo60.png)\n" +
                "\n" +
                "[![Build Status](https://travis-ci.org/joaoevangelista/curly.svg)](https://travis-ci.org/joaoevangelista/curly)\n" +
                "\n" +
                "[![forthebadge](http://forthebadge.com/images/badges/compatibility-betamax.svg)](http://forthebadge.com)\n" +
                "\n" +
                "[![forthebadge](http://forthebadge.com/images/badges/built-with-love.svg)](http://forthebadge.com)\n" +
                "\n" +
                "[![forthebadge](http://forthebadge.com/images/badges/gluten-free.svg)](http://forthebadge.com)\n" +
                "---------------------------------------------------------------------------"
        mongoTemplate.insert(paper)
        paper
    }

}
