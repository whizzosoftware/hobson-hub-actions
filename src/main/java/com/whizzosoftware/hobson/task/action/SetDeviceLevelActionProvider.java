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

public class SetDeviceLevelActionProvider extends ActionProvider {
    private static final String LEVEL = "level";

    private final ActionsPlugin plugin;

    public SetDeviceLevelActionProvider(ActionsPlugin plugin) {
        super(
            PropertyContainerClassContext.create(plugin.getContext(), "setLevel"),
            "Set dimmer to level",
            "Set {devices} level to {" + LEVEL + "}",
            true,
            1000
        );
        this.plugin = plugin;
        addSupportedProperty(
            new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
                constraint(PropertyConstraintType.required, true).
                constraint(PropertyConstraintType.deviceVariable, VariableConstants.LEVEL).
                build()
        );
        addSupportedProperty(
            new TypedProperty.Builder(LEVEL, "Level", "The percent level to set (0-100)", TypedProperty.Type.NUMBER).
                    constraint(PropertyConstraintType.required, true).
                    build()
        );
    }

    @Override
    public Action createAction(Map<String, Object> properties) {
        return new SetDeviceLevelAction(plugin.getContext(), new DeviceControlExecutionContext(plugin, properties),plugin.getEventLoopExecutor());
    }

    private class SetDeviceLevelAction extends AbstractVariableUpdateAction {
        SetDeviceLevelAction(PluginContext pluginContext, ActionExecutionContext executionContext, EventLoopExecutor executor) {
            super(pluginContext, executionContext, executor);
        }

        @Override
        String getVariableName() {
            return VariableConstants.LEVEL;
        }

        @Override
        Object getVariableValue(Map<String, Object> properties) {
            return properties.get(LEVEL);
        }
    }
}
