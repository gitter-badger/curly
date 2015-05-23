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

import curly.commons.security.OwnedResource;
import curly.commons.security.SimpleUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author Joao Pedro Evangelista
 */
@Slf4j
public abstract class ResourceOperationsResolverAdapter<T extends OwnedResource, U extends SimpleUser<Long>>
        implements ResourceOperationsResolver<T, U> {


    @Override
    public void checkMatch(T entity, U user) {
        Assert.notNull(entity, "Entity must be not null!");
        Assert.notNull(user, "User must be not null");

        if (isNewOwner(entity.getOwner())) {
            entity.setOwner(user.getId().toString());
        } else if (!isOwnedBy(entity, user)) {
            throw new OperationViolationException("Cannot match owner id with user's");
        }
    }

    private boolean isNewOwner(String owner) {
        return (owner == null) || owner.isEmpty();
    }

    /**
     * Resolve if the entity belongs to this user
     *
     * @param entity the to be checked
     * @param user   normally a class representing a user get on SecurityContext
     * @return true if the user owns this entity
     */
    protected boolean isOwnedBy(T entity, U user) {
        log.trace("Resolving ownership for entity {} of user {}", entity, user);
        return (entity.getOwner().equals(String.valueOf(user.getId())));
    }


}
