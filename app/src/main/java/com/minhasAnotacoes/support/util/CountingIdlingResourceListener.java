package com.minhasAnotacoes.support.util;

/**
 * Interface to notify of resources being busy or idle
 */
public interface CountingIdlingResourceListener {
    void increment();
    void decrement();
}

