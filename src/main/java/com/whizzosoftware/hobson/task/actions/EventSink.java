/*******************************************************************************
 * Copyright (c) 2015 Whizzo Software, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.whizzosoftware.hobson.task.actions;

import com.whizzosoftware.hobson.api.event.HobsonEvent;

/**
 * An interface that abstracts the sending of events from the actual runtime.
 *
 * @author Dan Noguerol
 */
public interface EventSink {
    public void postEvent(HobsonEvent event);
}
