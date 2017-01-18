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

import com.whizzosoftware.hobson.api.action.Action;
import com.whizzosoftware.hobson.api.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.action.ActionProvider;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.task.ActionsPlugin;
import com.whizzosoftware.hobson.task.action.context.DeviceControlExecutionContext;

import java.util.Map;

public class SetDeviceColorActionProvider extends ActionProvider {
    private static final String COLOR = "color";

    private final ActionsPlugin plugin;

    public SetDeviceColorActionProvider(ActionsPlugin plugin) {
        super(
            PropertyContainerClassContext.create(plugin.getContext(), "setColor"),
            "Set bulb color",
            "Set {devices} to {" + COLOR + "}",
            true,
            1000
        );
        this.plugin = plugin;
        addSupportedProperty(
            new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
                constraint(PropertyConstraintType.required, true).
                constraint(PropertyConstraintType.deviceVariable, VariableConstants.COLOR).
                build()
        );
        addSupportedProperty(
            new TypedProperty.Builder(COLOR, "Color", "The color to set", TypedProperty.Type.COLOR).
                constraint(PropertyConstraintType.required, true).
                build()
        );
    }

    @Override
    public Action createAction(Map<String, Object> properties) {
        return new SetDeviceColorAction(plugin.getContext(), new DeviceControlExecutionContext(plugin, properties),plugin.getEventLoopExecutor());
    }

    private class SetDeviceColorAction extends AbstractVariableUpdateAction {
        SetDeviceColorAction(PluginContext pluginContext, ActionExecutionContext executionContext, EventLoopExecutor executor) {
            super(pluginContext, executionContext, executor);
        }

        @Override
        String getVariableName() {
            return VariableConstants.COLOR;
        }

        @Override
        Object getVariableValue(Map<String, Object> properties) {
            return properties.get(COLOR);
        }
    }

}
