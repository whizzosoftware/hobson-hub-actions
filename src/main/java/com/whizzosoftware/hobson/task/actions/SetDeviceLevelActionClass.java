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
import com.whizzosoftware.hobson.api.task.action.ActionExecutionContext;
import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * An action class for setting a device's level variable.
 *
 * @author Dan Noguerol
 */
public class SetDeviceLevelActionClass extends AbstractVariableUpdateActionClass {
    public static final String LEVEL = "level";

    public SetDeviceLevelActionClass(PluginContext context, ActionExecutionContext actionExecutionContext) {
        super(context, "setLevel", "Set dimmer or switch levels", "Set {devices} level to {" + LEVEL + "}", actionExecutionContext);
    }

    @Override
    protected String getVariableName() {
        return VariableConstants.LEVEL;
    }

    @Override
    protected Object getVariableValue(Map<String, Object> propertyValues) {
        return propertyValues.get(LEVEL);
    }

    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("devices", "Devices", "The devices to send the command to", TypedProperty.Type.DEVICES).
            constraint(PropertyConstraintType.deviceVariable, VariableConstants.LEVEL).
            constraint(PropertyConstraintType.required, true).
            build()
        );
        props.add(new TypedProperty.Builder("level", "Level", "The percent level to set (0-100)", TypedProperty.Type.NUMBER).
            constraint(PropertyConstraintType.required, true).
            build()
        );
        return props;
    }
}
