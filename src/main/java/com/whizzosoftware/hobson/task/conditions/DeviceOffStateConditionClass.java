/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.conditions;

import com.whizzosoftware.hobson.api.device.DeviceContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.property.TypedPropertyConstraint;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.ConditionEvaluationContext;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.api.variable.VariableManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * An evaluator condition class that checks if a device is off.
 *
 * @author Dan Noguerol
 */
public class DeviceOffStateConditionClass extends TaskConditionClass {
    public static final String ID = "deviceOff";

    public DeviceOffStateConditionClass(PluginContext context) {
        super(PropertyContainerClassContext.create(context, ID), "Device(s) is/are off", "{devices} is/are off");
    }

    @Override
    public ConditionClassType getType() {
        return ConditionClassType.evaluator;
    }

    @Override
    public boolean evaluate(ConditionEvaluationContext context, PropertyContainer values) {
        VariableManager variableManager = context.getVariableManager();
        Collection<DeviceContext> deviceContexts = (Collection<DeviceContext>)values.getPropertyValue("devices");
        for (DeviceContext dctx : deviceContexts) {
            HobsonVariable v = variableManager.getDeviceVariable(dctx, VariableConstants.ON);
            if ((Boolean)v.getValue()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty("devices", "Devices", "The device(s) that should be off", TypedProperty.Type.DEVICES, Collections.singletonMap(TypedPropertyConstraint.deviceVariable, VariableConstants.ON)));
        return props;
    }
}
