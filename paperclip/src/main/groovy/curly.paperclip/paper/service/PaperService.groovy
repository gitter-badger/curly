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
package curly.paperclip.paper.service

import curly.paperclip.paper.model.Paper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.util.Assert

/**
 * @author João Evangelista
 */
@Service
class PaperService {

    private final PaperRepository repository

    @Autowired
    PaperService(PaperRepository repository) {
        Assert.notNull(repository, "PaperRepository, must be not null!")
        this.repository = repository
    }

    @Cacheable(value = ["paper"], key = "#item")
    Paper findByItem(String item) {
        this.repository.findByItem(item)
    }

    @Cacheable(value = ["paper"], key = "#item")
    Paper findByItemAndOwner(String item, String owner) {
        this.repository.findByItemAndOwner(item, owner)
    }


    @CacheEvict(value = ["paper"], key = "#paper.item")
    void delete(Paper paper) {
        this.repository.delete(paper)
    }

    @CachePut(value = ["paper"], key = "#paper.item")
    Paper save(Paper paper) {
        this.repository.save(paper)
    }

}
