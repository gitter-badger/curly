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
package curly.commons.web.hateoas;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author João Pedro Evangelista
 */
public class PageProcessor {

    public static <T> Page<T> toPage(Collection<T> list, PageMetadata metadata) {
        Assert.notNull(list, "List content must be not null");
        return new PageImpl<T>(new ArrayList<>(list), new PageRequest((int) metadata.getNumber(), (int) metadata.getSize()), metadata.getTotalElements());
    }

    public static <T> Page<T> toPage(PagedResources<T> res) {
        Assert.notNull(res, "Resources content must be not null");
        PageMetadata metadata = res.getMetadata();
        if (res.getContent().isEmpty()) return new PageImpl<T>(Collections.emptyList());
        return new PageImpl<>(new ArrayList<>(res.getContent()), new PageRequest((int) metadata.getNumber(),
                (int) metadata.getSize()), metadata.getTotalElements());
    }


    public static <T> List<T> fromPage(Page<T> page) {
        Assert.notNull(page, "Page element must be not null");
        return page.getContent();
    }

}
