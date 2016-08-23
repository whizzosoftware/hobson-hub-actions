/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.actions;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.event.VariableUpdateRequestEvent;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;
import com.whizzosoftware.hobson.api.variable.VariableContext;
import com.whizzosoftware.hobson.api.variable.VariableUpdate;
import java.util.*;

/**
 * An abstract class that provides convenience methods for action classes that update a device
 * variable.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractVariableUpdateActionClass extends TaskActionClass implements TaskActionExecutor {
    private EventSink eventSink;

    public AbstractVariableUpdateActionClass(PluginContext context, String id, String name, String descriptionTemplate, EventSink eventSink) {
        super(context, id, name, descriptionTemplate);
        this.eventSink = eventSink;
    }

    @Override
    public TaskActionExecutor getExecutor() {
        return this;
    }

    public void executeAction(PropertyContainer pc) {
        eventSink.postEvent(
            new VariableUpdateRequestEvent(
                System.currentTimeMillis(),
                createVariableUpdates(pc.getPropertyValues())
            )
        );
    }

    protected List<VariableUpdate> createVariableUpdates(Map<String, Object> propertyValues) {
        if (propertyValues.containsKey("device")) {
            DeviceContext ctx = (DeviceContext)propertyValues.get("device");
            return Collections.singletonList(new VariableUpdate(VariableContext.create(ctx, getVariableName()), getVariableValue(propertyValues)));
        } else if (propertyValues.containsKey("devices")) {
            List<VariableUpdate> results = new ArrayList<>();
            List<DeviceContext> contexts = (List<DeviceContext>)propertyValues.get("devices");
            for (DeviceContext ctx : contexts) {
                results.add(new VariableUpdate(VariableContext.create(ctx, getVariableName()), getVariableValue(propertyValues)));
            }
            return results;
        } else {
            throw new HobsonRuntimeException("No devices found to turn on in task action data");
        }
    }

    abstract protected String getVariableName();
    abstract protected Object getVariableValue(Map<String,Object> propertyValues);
}
