/**
 * Copyright 2009 the original author or authors
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
package org.springframework.extensions.jcr.support;

import javax.jcr.Repository;

import org.springframework.extensions.jcr.SessionHolderProvider;
import org.springframework.util.CachingMapDecorator;

/**
 * Manager which caches providers in order to avoid lookups.
 * @author Costin Leau
 * @author Sergio Bossa
 * @author Salvatore Incandela
 */
public abstract class CacheableSessionHolderProviderManager extends AbstractSessionHolderProviderManager {

    /**
     * Caching class based on CachingMapDecorator from main Spring distribution.
     * @author Costin Leau
     * @author Sergio Bossa
     * @author Salvatore Incandela
     */
    protected class ProvidersCache extends CachingMapDecorator {
        private ProvidersCache() {
            super(true);
        }

        /**
         * @see org.springframework.util.CachingMapDecorator#create(java.lang.Object)
         */
        protected Object create(Object key) {
            return parentLookup((Repository) key);
        }

    }

    /**
     * Providers cache.
     */
    private final ProvidersCache providersCache = new ProvidersCache();

    /**
     * Method for retrieving the parent functionality.
     * @param repository
     * @return
     */
    private SessionHolderProvider parentLookup(Repository repository) {
        return super.getSessionProvider(repository);
    }

    /**
     * Overwrite the method to provide caching.
     * @see org.springframework.extensions.jcr.support.AbstractSessionHolderProviderManager#getSessionProvider(Repository)
     */
    public SessionHolderProvider getSessionProvider(Repository repository) {
        return (SessionHolderProvider) providersCache.get(repository);
    }

}
