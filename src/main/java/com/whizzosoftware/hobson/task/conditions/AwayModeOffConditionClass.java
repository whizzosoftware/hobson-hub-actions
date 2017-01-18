/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task.conditions;

import com.whizzosoftware.hobson.api.hub.HubConfigurationClass;
import com.whizzosoftware.hobson.api.hub.HubContext;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.condition.ConditionClassType;
import com.whizzosoftware.hobson.api.task.condition.ConditionEvaluationContext;
import com.whizzosoftware.hobson.api.task.condition.TaskConditionClass;

import java.util.ArrayList;
import java.util.List;

public class AwayModeOffConditionClass extends TaskConditionClass {
    private static final String ID = "notAway";

    public AwayModeOffConditionClass(PluginContext context) {
        super(PropertyContainerClassContext.create(context, ID), "Away mode off", "The hub is not in away mode");
    }

    @Override
    public ConditionClassType getConditionClassType() {
        return ConditionClassType.evaluator;
    }

    @Override
    public boolean evaluate(ConditionEvaluationContext context, PropertyContainer values) {
        PropertyContainer pc = context.getHubConfiguration(HubContext.createLocal());
        return !pc.getBooleanPropertyValue(HubConfigurationClass.AWAY);
    }

    @Override
    protected List<TypedProperty> createProperties() {
        return new ArrayList<>();
    }
}
