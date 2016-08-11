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
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.task.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;

import java.util.*;

/**
 * An abstract class that provides convenience methods for action classes that update a device
 * variable.
 *
 * @author Dan Noguerol
 */
abstract public class AbstractVariableUpdateActionClass extends TaskActionClass implements TaskActionExecutor {
    private ActionExecutionContext actionExecutionContext;

    public AbstractVariableUpdateActionClass(PluginContext context, String id, String name, String descriptionTemplate, ActionExecutionContext actionExecutionContext) {
        super(context, id, name, descriptionTemplate);
        this.actionExecutionContext = actionExecutionContext;
    }

    @Override
    public TaskActionExecutor getExecutor() {
        return this;
    }

    public void executeAction(PropertyContainer pc) {
        actionExecutionContext.setDeviceVariables(createVariableUpdates(pc.getPropertyValues()));
    }

    protected Map<DeviceVariableContext,Object> createVariableUpdates(Map<String, Object> propertyValues) {
        if (propertyValues.containsKey("device")) {
            DeviceContext ctx = (DeviceContext)propertyValues.get("device");
            return Collections.singletonMap(DeviceVariableContext.create(ctx, getVariableName()), getVariableValue(propertyValues));
        } else if (propertyValues.containsKey("devices")) {
            Map<DeviceVariableContext,Object> results = new HashMap<>();
            List<DeviceContext> contexts = (List<DeviceContext>)propertyValues.get("devices");
            for (DeviceContext ctx : contexts) {
                results.put(DeviceVariableContext.create(ctx, getVariableName()), getVariableValue(propertyValues));
            }
            return results;
        } else {
            throw new HobsonRuntimeException("No devices found to turn on in task action data");
        }
    }

    abstract protected String getVariableName();
    abstract protected Object getVariableValue(Map<String,Object> propertyValues);
}
