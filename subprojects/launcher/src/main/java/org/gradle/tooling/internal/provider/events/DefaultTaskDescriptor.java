/*
 * Copyright 2015 the original author or authors.
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

package org.gradle.tooling.internal.provider.events;

import org.gradle.tooling.internal.protocol.events.InternalTaskWithDependenciesDescriptor;

import java.io.Serializable;
import java.util.Set;

public class DefaultTaskDescriptor implements Serializable, InternalTaskWithDependenciesDescriptor {

    private final Object id;
    private final String taskIdentityPath;
    private final String displayName;
    private final String taskPath;
    private final Object parentId;
    private final Set<DefaultTaskDescriptor> dependencies;

    public DefaultTaskDescriptor(Object id, String taskIdentityPath, String taskPath, String displayName, Object parentId, Set<DefaultTaskDescriptor> dependencies) {
        this.id = id;
        this.taskIdentityPath = taskIdentityPath;
        this.displayName = displayName;
        this.taskPath = taskPath;
        this.dependencies = dependencies;
        this.parentId = parentId;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public String getName() {
        return taskIdentityPath;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getTaskPath() {
        return taskPath;
    }

    @Override
    public Object getParentId() {
        return parentId;
    }

    @Override
    public Set<DefaultTaskDescriptor> getDependencies() {
        return dependencies;
    }

}
