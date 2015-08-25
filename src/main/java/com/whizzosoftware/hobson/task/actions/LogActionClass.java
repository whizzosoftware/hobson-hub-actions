/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.actions;

import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.action.TaskActionClass;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * An action class for sending a message to the log file.
 *
 * @author Dan Noguerol
 */
public class LogActionClass extends TaskActionClass implements TaskActionExecutor {
    public static final String MESSAGE = "message";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public LogActionClass(PluginContext context) {
        super(context, "log", "Log a message", "Log \"{message}\"");
    }

    @Override
    public TaskActionExecutor getExecutor() {
        return this;
    }

    @Override
    public void executeAction(PropertyContainer pc) {
        String message = (String)pc.getPropertyValues().get(MESSAGE);
        if (message != null) {
            logger.info(message);
        } else {
            logger.error("No log message specified; unable to execute log action");
        }
    }

    @Override
    protected List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty("message", "Message", "The message added to the log file", TypedProperty.Type.STRING));
        return props;
    }
}
