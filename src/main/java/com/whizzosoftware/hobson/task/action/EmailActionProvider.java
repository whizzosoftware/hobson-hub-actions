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
import com.whizzosoftware.hobson.task.action.context.HubControlExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EmailActionProvider extends ActionProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String RECIPIENT_ADDRESS = "recipientAddress";
    private static final String SUBJECT = "subject";
    private static final String MESSAGE = "message";

    private final ActionsPlugin plugin;

    public EmailActionProvider(ActionsPlugin plugin) {
        super(
            PropertyContainerClassContext.create(plugin.getContext(), "email"),
            "Send an e-mail",
            "Send an e-mail to {" + RECIPIENT_ADDRESS + "} with subject {" + SUBJECT + "}",
            true,
            1000
        );
        this.plugin = plugin;
        addSupportedProperty(
                new TypedProperty.Builder(RECIPIENT_ADDRESS, "Recipient Address", "The e-mail address to send the message to", TypedProperty.Type.STRING).
                        constraint(PropertyConstraintType.required, true).
                        build()
        );
        addSupportedProperty(
                new TypedProperty.Builder(SUBJECT, "Subject", "The e-mail subject line", TypedProperty.Type.STRING).
                        constraint(PropertyConstraintType.required, true).
                        build()
        );
        addSupportedProperty(
                new TypedProperty.Builder(MESSAGE, "Message", "The e-mail body text", TypedProperty.Type.STRING).
                        constraint(PropertyConstraintType.required, true).
                        build()
        );
    }

    @Override
    public Action createAction(Map<String, Object> properties) {
        return new EmailAction(plugin.getContext(), new HubControlExecutionContext(plugin, properties),plugin.getEventLoopExecutor());
    }

    private class EmailAction extends SingleAction {

        EmailAction(PluginContext pctx, HubControlExecutionContext ectx, EventLoopExecutor executor) {
            super(pctx, ectx, executor);
        }

        @Override
        public void onStart(ActionLifecycleContext ctx) {
            Map<String,Object> properties = getContext().getProperties();

            String recipient = (String)properties.get(RECIPIENT_ADDRESS);
            String subject = (String)properties.get(SUBJECT);
            String body = (String)properties.get(MESSAGE);

            try {
                ((HubControlExecutionContext)getContext()).sendEmail(recipient, subject, body);
                ctx.complete();
            } catch (Throwable t) {
                logger.error("Failed to send e-mail", t);
                ctx.fail("Failed to send e-mail. See the log for details");
            }
        }

        @Override
        public void onMessage(ActionLifecycleContext ctx, String name, Object prop) {
        }

        @Override
        public void onStop(ActionLifecycleContext ctx) {
        }
    }
}
