/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.api.internal.artifacts.ivyservice.modulecache.dynamicversions;

import com.google.common.collect.Maps;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.artifacts.VersionVariants;
import org.gradle.api.internal.artifacts.ivyservice.ivyresolve.ModuleComponentRepository;
import org.gradle.util.BuildCommencedTimeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class InMemoryModuleVersionsCache implements ModuleVersionsCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultModuleVersionsCache.class);
    private final BuildCommencedTimeProvider timeProvider;
    private final Map<ModuleAtRepositoryKey, ModuleVersionsCacheEntry> inMemoryCache = Maps.newConcurrentMap();

    public InMemoryModuleVersionsCache(BuildCommencedTimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    public void cacheModuleVersionList(ModuleComponentRepository repository, ModuleIdentifier moduleId, Set<VersionVariants> listedVersions) {
        LOGGER.debug("Caching version list in module versions cache: Using '{}' for '{}'", listedVersions, moduleId);
        ModuleAtRepositoryKey key = createKey(repository, moduleId);
        ModuleVersionsCacheEntry entry = createEntry(listedVersions);
        store(key, entry);
    }

    protected void store(ModuleAtRepositoryKey key, ModuleVersionsCacheEntry entry) {
        inMemoryCache.put(key, entry);
    }

    public CachedModuleVersionList getCachedModuleResolution(ModuleComponentRepository repository, ModuleIdentifier moduleId) {
        ModuleAtRepositoryKey key = createKey(repository, moduleId);
        ModuleVersionsCacheEntry entry = get(key);
        return entry == null ? null : versionList(entry);
    }

    protected ModuleVersionsCacheEntry get(ModuleAtRepositoryKey key) {
        return inMemoryCache.get(key);
    }

    private CachedModuleVersionList versionList(ModuleVersionsCacheEntry moduleVersionsCacheEntry) {
        return new DefaultCachedModuleVersionList(moduleVersionsCacheEntry, timeProvider);
    }

    private ModuleAtRepositoryKey createKey(ModuleComponentRepository repository, ModuleIdentifier moduleId) {
        return new ModuleAtRepositoryKey(repository.getId(), moduleId);
    }

    private ModuleVersionsCacheEntry createEntry(Set<VersionVariants> listedVersions) {
        return new ModuleVersionsCacheEntry(listedVersions, timeProvider.getCurrentTime());
    }
}
