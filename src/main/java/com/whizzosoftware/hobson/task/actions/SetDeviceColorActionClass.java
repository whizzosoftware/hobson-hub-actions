/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.actions;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An action class for setting a device's color variable.
 *
 * @author Dan Noguerol
 */
public class SetDeviceColorActionClass extends AbstractVariableUpdateActionClass {
    public static final String COLOR = "color";

    public SetDeviceColorActionClass(PluginContext context, EventSink eventSink) {
        super(context, "setColor", "Set bulb colors", "Set {devices} to {" + COLOR + "}", eventSink);
    }

    @Override
    protected String getVariableName() {
        return VariableConstants.COLOR;
    }

    @Override
    protected Object getVariableValue(Map<String,Object> propertyValues) {
        return propertyValues.get(COLOR);
    }

    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
            constraint(PropertyConstraintType.required, true).
            constraint(PropertyConstraintType.deviceVariable, VariableConstants.COLOR).
            build()
        );
        props.add(new TypedProperty.Builder(COLOR, "Color", "The color to set", TypedProperty.Type.COLOR).
            constraint(PropertyConstraintType.required, true).
            build()
        );
        return props;
    }
}
