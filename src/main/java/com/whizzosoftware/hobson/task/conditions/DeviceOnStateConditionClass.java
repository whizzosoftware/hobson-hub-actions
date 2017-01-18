/*
 *******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task.conditions;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.*;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.ConditionEvaluationContext;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.variable.DeviceVariableContext;
import com.whizzosoftware.hobson.api.variable.DeviceVariableState;
import com.whizzosoftware.hobson.api.variable.VariableConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An evaluator condition class that checks if a device is on.
 *
 * @author Dan Noguerol
 */
public class DeviceOnStateConditionClass extends TaskConditionClass {
    public static final String ID = "deviceOn";

    public DeviceOnStateConditionClass(PluginContext context) {
        super(PropertyContainerClassContext.create(context, ID), "Device(s) is/are on", "{devices} is/are on");
    }

    @Override
    public ConditionClassType getConditionClassType() {
        return ConditionClassType.evaluator;
    }

    @Override
    public boolean evaluate(ConditionEvaluationContext context, PropertyContainer values) {
        Collection<DeviceContext> deviceContexts = (Collection<DeviceContext>)values.getPropertyValue("devices");
        for (DeviceContext dctx : deviceContexts) {
            DeviceVariableState v = context.getDeviceVariableState(DeviceVariableContext.create(dctx, VariableConstants.ON));
            if (!(Boolean)v.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty.Builder("devices", "Devices", "The device(s) that should be on", TypedProperty.Type.DEVICES).
            constraint(PropertyConstraintType.required, true).
            constraint(PropertyConstraintType.deviceVariable, VariableConstants.ON).
            build()
        );
        return props;
    }
}
