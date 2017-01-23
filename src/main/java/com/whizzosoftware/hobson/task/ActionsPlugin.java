/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.event.device.DeviceVariablesUpdateRequestEvent;
import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import com.whizzosoftware.hobson.api.plugin.PluginType;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.task.conditions.DeviceOffStateConditionClass;
import com.whizzosoftware.hobson.task.conditions.DeviceOnStateConditionClass;
import com.whizzosoftware.hobson.task.conditions.ManualTaskExecutionConditionClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.task.action.*;
import com.whizzosoftware.hobson.task.conditions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * A plugin that registers some "core" actions with the runtime.
 *
 * @author Dan Noguerol
 */
public class ActionsPlugin extends AbstractHobsonPlugin {
    public ActionsPlugin(String pluginId, String version, String description) {
        super(pluginId, version, description);
    }

    @Override
    public String getName() {
        return "Hobson Actions";
    }

    @Override
    public PluginType getType() {
        return PluginType.CORE;
    }

    @Override
    public void onStartup(PropertyContainer config) {
        // publish trigger conditions
        publishTaskConditionClass(new ManualTaskExecutionConditionClass(getContext()));

        // publish default evaluator conditions
        publishTaskConditionClass(new DeviceOnStateConditionClass(getContext()));
        publishTaskConditionClass(new DeviceOffStateConditionClass(getContext()));
        publishTaskConditionClass(new AwayModeOnConditionClass(getContext()));
        publishTaskConditionClass(new AwayModeOffConditionClass(getContext()));

        // publish default actions
        publishActionProvider(new ArmDeviceActionProvider(this));
        publishActionProvider(new DisarmDeviceActionProvider(this));
        publishActionProvider(new EmailActionProvider(this));
        publishActionProvider(new LogActionProvider(this));
        publishActionProvider(new SetDeviceLevelActionProvider(this));
        publishActionProvider(new SetDeviceColorActionProvider(this));
        publishActionProvider(new TurnDeviceOnActionProvider(this));
        publishActionProvider(new TurnDeviceOffActionProvider(this));
    }

    @Override
    public void onShutdown() {
    }

    @Override
    public void onPluginConfigurationUpdate(PropertyContainer config) {
    }

    @Override
    protected TypedProperty[] getConfigurationPropertyTypes() {
        return null;
    }

    public void doSetDeviceVariables(Map<DeviceVariableContext, Object> values) {
        Map<DeviceContext,Map<String,Object>> varMap = new HashMap<>();
        for (DeviceVariableContext ctx : values.keySet()) {
            Map<String,Object> m = varMap.get(ctx.getDeviceContext());
            if (m == null) {
                m = new HashMap<>();
                varMap.put(ctx.getDeviceContext(), m);
            }
            m.put(ctx.getName(), values.get(ctx));
        }

        long now = System.currentTimeMillis();

        for (DeviceContext dctx : varMap.keySet()) {
            Map<String,Object> vals = varMap.get(dctx);
            postEvent(new DeviceVariablesUpdateRequestEvent(now, dctx, vals));
        }
    }

    public void sendEmail(String recipient, String subject, String body) {
        getHubManager().sendEmail(getContext().getHubContext(), recipient, subject, body);
    }
}
