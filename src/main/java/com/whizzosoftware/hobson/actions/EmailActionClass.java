/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.actions;

import com.whizzosoftware.hobson.api.hub.HubManager;
import com.whizzosoftware.hobson.api.plugin.PluginContext;
import com.whizzosoftware.hobson.api.property.PropertyContainer;
import com.whizzosoftware.hobson.api.property.TypedProperty;
import com.whizzosoftware.hobson.api.task.TaskActionClass;
import com.whizzosoftware.hobson.api.task.action.TaskActionExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmailActionClass extends TaskActionClass implements TaskActionExecutor {
    public static final String RECIPIENT_ADDRESS = "recipientAddress";
    public static final String SUBJECT = "subject";
    public static final String MESSAGE = "message";

    private HubManager hubManager;

    public EmailActionClass(PluginContext context, HubManager hubManager) {
        super(context, "email", "Send an e-mail");

        this.hubManager = hubManager;
    }

    @Override
    public List<TypedProperty> createProperties() {
        List<TypedProperty> props = new ArrayList<>();
        props.add(new TypedProperty(RECIPIENT_ADDRESS, "Recipient Address", "The e-mail address to send the message to", TypedProperty.Type.STRING));
        props.add(new TypedProperty(SUBJECT, "Subject", "The e-mail subject line", TypedProperty.Type.STRING));
        props.add(new TypedProperty(MESSAGE, "Message", "The e-mail body text", TypedProperty.Type.STRING));
        return props;
    }


    @Override
    public TaskActionExecutor getExecutor() {
        return this;
    }

    @Override
    public void executeAction(PropertyContainer pc) {
        Map<String,Object> propertyValues = pc.getPropertyValues();

        // send the e-mail
        hubManager.sendEmail(
            getContext().getHubContext(),
            (String)propertyValues.get(RECIPIENT_ADDRESS),
            (String)propertyValues.get(SUBJECT),
            (String)propertyValues.get(MESSAGE)
        );
    }
}
