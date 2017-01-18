/*
 *******************************************************************************
 * Copyright (c) 2016 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************
*/
package com.whizzosoftware.hobson.task.action;

import com.whizzosoftware.hobson.api.action.*;
import com.whizzosoftware.hobson.api.plugin.EventLoopExecutor;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyConstraintType;
import com.whizzosoftware.hobson.api.property.PropertyContainerClassContext;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.task.ActionsPlugin;
import com.whizzosoftware.hobson.task.action.context.DeviceControlExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LogActionProvider extends ActionProvider {
    private static final String MESSAGE = "message";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ActionsPlugin plugin;

    public LogActionProvider(ActionsPlugin plugin) {
        super(
            PropertyContainerClassContext.create(plugin.getContext(), "log"),
            "Log a message",
            "Log \"{" + MESSAGE + "}\"",
            true,
            1000
        );
        this.plugin = plugin;
        addSupportedProperty(
            new TypedProperty.Builder(MESSAGE, "Message", "The message added to the log file", TypedProperty.Type.STRING).
                constraint(PropertyConstraintType.required, true).
                build()
        );
    }

    @Override
    public Action createAction(final Map<String, Object> properties) {
        return new LogAction(plugin.getContext(), new DeviceControlExecutionContext(plugin, properties),plugin.getEventLoopExecutor());
    }

    private class LogAction extends SingleAction {

        LogAction(PluginContext pctx, DeviceControlExecutionContext ectx, EventLoopExecutor executor) {
            super(pctx, ectx, executor);
        }

        @Override
        public void onStart(ActionLifecycleContext ctx) {
            Map<String,Object> properties = getContext().getProperties();
            String msg = (String)properties.get(MESSAGE);
            if (msg != null) {
                logger.error(msg);
            }
            ctx.complete();
        }

        @Override
        public void onMessage(ActionLifecycleContext ctx, String name, Object prop) {
        }

        @Override
        public void onStop(ActionLifecycleContext ctx) {
        }
    }
}
