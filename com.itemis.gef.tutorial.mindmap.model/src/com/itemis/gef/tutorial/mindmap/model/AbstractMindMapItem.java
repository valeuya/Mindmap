package com.itemis.gef.tutorial.mindmap.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * This class provides the {@link PropertyChangeSupport} for the MindMapitems
 *
 */
public class AbstractMindMapItem implements Serializable {

    /**
     * Generated UUID
     */
    private static final long serialVersionUID = -2558628513984118991L;

    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
