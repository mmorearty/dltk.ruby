/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.ruby.internal.ui.docs;

import org.eclipse.core.runtime.ListenerList;

public class Watchdog implements Runnable {

	private ListenerList timeoutListeners = new ListenerList();

	private final long timeout;

	private boolean stopped = false;

	public Watchdog(final long timeout) {
		if (timeout < 1) {
			throw new IllegalArgumentException("timeout lesser than 1."); //$NON-NLS-1$
		}
		this.timeout = timeout;
	}

	public void addListener(WatchdogListener listener) {
		timeoutListeners.add(listener);
	}

	public void removeListener(WatchdogListener listener) {
		timeoutListeners.remove(listener);
	}

	protected final void fireTimeoutOccured() {
		Object[] listeners = timeoutListeners.getListeners();
		for (int i = 0; i < listeners.length; ++i) {
			((WatchdogListener) listeners[i]).timeoutOccured();
		}
	}

	public synchronized void start() {
		stopped = false;
		final Thread t = new Thread(this, "WATCHDOG"); //$NON-NLS-1$
		t.setDaemon(true);
		t.start();
	}

	public synchronized void stop() {
		stopped = true;
		notifyAll();
	}

	public synchronized void run() {
		final long until = System.currentTimeMillis() + timeout;
		long now;
		while (!stopped && until > (now = System.currentTimeMillis())) {
			try {
				wait(until - now);
			} catch (InterruptedException e) {
			}
		}
		if (!stopped) {
			fireTimeoutOccured();
		}
	}
}
