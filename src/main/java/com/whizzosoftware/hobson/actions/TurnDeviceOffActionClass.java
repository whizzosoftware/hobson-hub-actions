/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.actions;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.property.TypedPropertyConstraint;
import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * An action class for setting a device's on variable to false.
 *
 * @author Dan Noguerol
 */
public class TurnDeviceOffActionClass extends AbstractVariableUpdateActionClass {

    public TurnDeviceOffActionClass(PluginContext context, EventSink eventSink) {
        super(context, "turnOff", "Turn off bulbs or switches", eventSink);
    }

    @Override
    public List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES, Collections.singletonMap(TypedPropertyConstraint.deviceVariable, VariableConstants.ON)));
        return props;
    }

    @Override
    protected String getVariableName() {
        return VariableConstants.ON;
    }

    @Override
    protected Object getVariableValue(Map<String, Object> propertyValues) {
        return false;
    }
}
