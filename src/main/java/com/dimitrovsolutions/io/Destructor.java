package com.dimitrovsolutions.io;

/**
 * Implemented by all classes using instances to clear resources such a logger handlers
 * and sockets.
 */
@FunctionalInterface
public interface Destructor {
    void tearDown();
}
