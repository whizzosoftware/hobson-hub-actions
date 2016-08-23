/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task;

import com.whizzosoftware.hobson.api.event.HobsonEvent;
import com.whizzosoftware.hobson.api.plugin.AbstractHobsonPlugin;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.task.actions.*;
import com.whizzosoftware.hobson.task.conditions.DeviceOffStateConditionClass;
import com.whizzosoftware.hobson.task.conditions.DeviceOnStateConditionClass;
import com.whizzosoftware.hobson.task.conditions.ManualTaskExecutionConditionClass;

/**
 * A plugin that registers some "core" actions with the runtime.
 *
 * @author Dan Noguerol
 */
public class ActionsPlugin extends AbstractHobsonPlugin implements EventSink {
    public ActionsPlugin(String pluginId) {
        super(pluginId);
    }

    @Override
    public String getName() {
        return "Hobson Actions";
    }

    @Override
    protected TypedProperty[] createSupportedProperties() {
        return null;
    }

    @Override
    public void onPluginConfigurationUpdate(PropertyContainer config) {
    }

    @Override
    public void onStartup(PropertyContainer config) {
        // publish trigger conditions
        publishConditionClass(new ManualTaskExecutionConditionClass(getContext()));

        // publish default evaluator conditions
        publishConditionClass(new DeviceOnStateConditionClass(getContext()));
        publishConditionClass(new DeviceOffStateConditionClass(getContext()));

        // publish default actions
        publishActionClass(new ArmDeviceActionClass(getContext(), this));
        publishActionClass(new DisarmDeviceActionClass(getContext(), this));
        publishActionClass(new EmailActionClass(getContext(), getHubManager()));
        publishActionClass(new LogActionClass(getContext()));
        publishActionClass(new TurnDeviceOnActionClass(getContext(), this));
        publishActionClass(new TurnDeviceOffActionClass(getContext(), this));
        publishActionClass(new SetDeviceLevelActionClass(getContext(), this));
        publishActionClass(new SetDeviceColorActionClass(getContext(), this));
    }

    @Override
    public void onShutdown() {

    }


    @Override
    public void postEvent(HobsonEvent event) {
        fireHobsonEvent(event);
    }
}
