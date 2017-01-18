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

import com.whizzosoftware.hobson.api.action.*;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.task.ActionsPlugin;
import com.whizzosoftware.hobson.task.action.context.DeviceControlExecutionContext;

import java.util.Map;

public class TurnDeviceOffActionProvider extends ActionProvider {
    private final ActionsPlugin plugin;

    public TurnDeviceOffActionProvider(ActionsPlugin plugin) {
        super(
            PropertyContainerClassContext.create(plugin.getContext(), "turnOff"),
            "Turn off bulbs or switches",
            "Turn off {devices}",
            true,
            1000
        );
        this.plugin = plugin;
        addSupportedProperty(
            new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
                constraint(PropertyConstraintType.required, true).
                constraint(PropertyConstraintType.deviceVariable, VariableConstants.ON).
                build()
        );
    }

    @Override
    public SingleAction createAction(final Map<String,Object> properties) {
        return new TurnDeviceOffAction(plugin.getContext(), new DeviceControlExecutionContext(plugin, properties), plugin.getEventLoopExecutor());
    }

    private class TurnDeviceOffAction extends AbstractVariableUpdateAction {
        TurnDeviceOffAction(PluginContext pluginContext, ActionExecutionContext executionContext, EventLoopExecutor executor) {
            super(pluginContext, executionContext, executor);
        }

        @Override
        String getVariableName() {
            return VariableConstants.ON;
        }

        @Override
        Object getVariableValue(Map<String, Object> properties) {
            return false;
        }
    }
}
