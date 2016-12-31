// Copyright (C) 2015 Ericsson
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.ericsson.gerrit.plugins.syncevents;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import com.google.gerrit.server.events.Event;
import com.google.gerrit.server.events.RefEvent;

import org.junit.Test;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class EventHandlerTest {
  private static final String PLUGIN_NAME = "sync-event";

  private Event event;
  private EventHandler eventHandler;
  private RestSession restClient;

  @Test
  public void testRightEventAndNotForwarded() throws Exception {
    setUpMocks(true);
    eventHandler.onEvent(event);
    verify(restClient).send(event);
  }

  @Test
  public void testRightEventIsForwarded() throws Exception {
    setUpMocks(true);
    Context.setForwardedEvent();
    eventHandler.onEvent(event);
    Context.unsetForwardedEvent();
    verifyZeroInteractions(restClient);
  }

  @Test
  public void testBadEventAndNotForwarded() throws Exception {
    setUpMocks(false);
    eventHandler.onEvent(event);
    verifyZeroInteractions(restClient);
  }

  @Test
  public void testBadEventAndItIsForwarded() throws Exception {
    setUpMocks(false);
    Context.setForwardedEvent();
    eventHandler.onEvent(event);
    Context.unsetForwardedEvent();
    verifyZeroInteractions(restClient);
  }

  private void setUpMocks(boolean rightEvent) {
    ScheduledThreadPoolExecutor pool = new PoolMock(1);
    restClient = mock(RestSession.class);
    if (rightEvent) {
      event = mock(RefEvent.class);
    } else {
      event = mock(Event.class);
    }
    eventHandler = new EventHandler(restClient, pool, PLUGIN_NAME);
  }

  private class PoolMock extends ScheduledThreadPoolExecutor {
    PoolMock(int corePoolSize) {
      super(corePoolSize);
    }

    @Override
    public void execute(Runnable command) {
      assertThat(command.toString()).isEqualTo(String.format(
          "[%s] Send event '%s' to target instance", PLUGIN_NAME, null));
      command.run();
    }
  }
}
