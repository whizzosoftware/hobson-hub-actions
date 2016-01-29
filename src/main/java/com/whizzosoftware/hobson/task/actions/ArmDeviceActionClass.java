/*******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.actions;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmDeviceActionClass extends AbstractVariableUpdateActionClass {
    public ArmDeviceActionClass(PluginContext context, EventSink eventSink) {
        super(context, "arm", "Arm devices", "Arm {devices}", eventSink);
    }

    @Override
    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
                constraint(PropertyConstraintType.required, true).
                constraint(PropertyConstraintType.deviceVariable, VariableConstants.ARMED).
                build()
        );
        return props;
    }

    @Override
    protected String getVariableName() {
        return VariableConstants.ARMED;
    }

    @Override
    protected Object getVariableValue(Map<String, Object> propertyValues) {
        return true;
    }
}
