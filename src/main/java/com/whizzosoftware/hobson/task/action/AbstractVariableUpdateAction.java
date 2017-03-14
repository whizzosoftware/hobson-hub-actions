/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task.action;

import com.whizzosoftware.hobson.api.HobsonRuntimeException;
import com.whizzosoftware.hobson.api.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.action.ActionLifecycleContext;
import com.whizzosoftware.hobson.api.action.SingleAction;
import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.task.action.context.DeviceControlExecutionContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
abstract public class AbstractVariableUpdateAction extends SingleAction {

    AbstractVariableUpdateAction(PluginContext pluginContext, ActionExecutionContext executionContext, EventLoopExecutor executor) {
        super(pluginContext, executionContext, executor);
    }

    abstract String getVariableName();
    abstract Object getVariableValue(Map<String,Object> properties);

    @Override
    public void onStart(ActionLifecycleContext ctx) {
        ((DeviceControlExecutionContext)getContext()).doSetDeviceVariables(createVariableUpdates(getContext().getProperties()));
        ctx.complete();
    }

    private Map<DeviceVariableContext,Object> createVariableUpdates(Map<String,Object> propertyValues) {
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
            throw new HobsonRuntimeException("No devices defined in task action data");
        }
    }

    @Override
    public void onMessage(ActionLifecycleContext ctx, String name, Object prop) {

    }

    @Override
    public void onStop(ActionLifecycleContext ctx) {

    }
}
