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
import com.whizzosoftware.hobson.api.task.condition.ConditionEvaluationContext;
import com.whizzosoftware.hobson.api.variable.HobsonVariable;
import com.whizzosoftware.hobson.api.variable.MockVariableManager;
import com.whizzosoftware.hobson.api.variable.VariableConstants;
import com.whizzosoftware.hobson.api.variable.VariableManager;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DeviceOnStateConditionClassTest {
    @Test
    public void testEvaluateOneTrueDevice() {
        PluginContext pctx = PluginContext.createLocal("plugin1");
        DeviceContext dctx = DeviceContext.create(pctx, "device1");
        DeviceOnStateConditionClass cclass = new DeviceOnStateConditionClass(pctx);

        MockConditionEvaluationContext cec = new MockConditionEvaluationContext();
        cec.publishVariable(dctx, VariableConstants.ON, true);

        Map<String,Object> values = new HashMap<>();
        List<DeviceContext> deviceList = new ArrayList<>();
        deviceList.add(dctx);
        values.put("devices", deviceList);
        PropertyContainer pcc = new PropertyContainer(cclass.getContext(), values);

        assertTrue(cclass.evaluate(cec, pcc));
    }

    @Test
    public void testEvaluateOneTrueOneFalseDevice() {
        PluginContext pctx = PluginContext.createLocal("plugin1");
        DeviceContext dctx1 = DeviceContext.create(pctx, "device1");
        DeviceContext dctx2 = DeviceContext.create(pctx, "device2");
        DeviceOnStateConditionClass cclass = new DeviceOnStateConditionClass(pctx);

        MockConditionEvaluationContext cec = new MockConditionEvaluationContext();
        cec.publishVariable(dctx1, VariableConstants.ON, true);
        cec.publishVariable(dctx2, VariableConstants.ON, false);

        Map<String, Object> values = new HashMap<>();
        List<DeviceContext> deviceList = new ArrayList<>();
        deviceList.add(dctx1);
        deviceList.add(dctx2);
        values.put("devices", deviceList);
        PropertyContainer pcc = new PropertyContainer(cclass.getContext(), values);

        assertFalse(cclass.evaluate(cec, pcc));
    }

    @Test
    public void testEvaluateOneFalseDevice() {
        PluginContext pctx = PluginContext.createLocal("plugin1");
        DeviceContext dctx = DeviceContext.create(pctx, "device1");
        DeviceOnStateConditionClass cclass = new DeviceOnStateConditionClass(pctx);

        MockConditionEvaluationContext cec = new MockConditionEvaluationContext();
        cec.publishVariable(dctx, VariableConstants.ON, false);

        Map<String,Object> values = new HashMap<>();
        List<DeviceContext> deviceList = new ArrayList<>();
        deviceList.add(dctx);
        values.put("devices", deviceList);
        PropertyContainer pcc = new PropertyContainer(cclass.getContext(), values);

        assertFalse(cclass.evaluate(cec, pcc));
    }

    public class MockConditionEvaluationContext implements ConditionEvaluationContext {
        private MockVariableManager variableManager;

        public MockConditionEvaluationContext() {
            variableManager = new MockVariableManager();
        }

        public void publishVariable(DeviceContext dctx, String name, Object value) {
            variableManager.publishDeviceVariable(dctx, name, value, HobsonVariable.Mask.READ_ONLY);
        }

        @Override
        public VariableManager getVariableManager() {
            return variableManager;
        }
    }
}
