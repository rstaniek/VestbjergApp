package com.teamSuperior.core.controlLayer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by rajmu on 17.02.08.
 */
public abstract class NotifyingThread extends Thread {

    private final Set<IThreadCompleteListener> listeners = new CopyOnWriteArraySet<IThreadCompleteListener>();

    public final void addListener(final IThreadCompleteListener listener) {
        listeners.add(listener);
    }

    public final void removeListener(final IThreadCompleteListener listener) {
        listeners.remove(listener);
    }

    private final void notifyListeners() {
        for (IThreadCompleteListener listener : listeners) {
            listener.onThreadComplete(this);
        }
    }

    @Override
    public void run() {
        try {
            runThread();
        } finally {
            notifyListeners();
        }
    }

    public abstract void runThread();
}
