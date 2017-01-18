/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task.action.context;

import com.whizzosoftware.hobson.api.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.task.ActionsPlugin;

import java.util.Map;

public class DeviceControlExecutionContext implements ActionExecutionContext {
    private ActionsPlugin plugin;
    private Map<String,Object> properties;

    public DeviceControlExecutionContext(ActionsPlugin plugin, Map<String, Object> properties) {
        this.plugin = plugin;
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void doSetDeviceVariables(Map<DeviceVariableContext,Object> values) {
        plugin.doSetDeviceVariables(values);
    }
}
