package com.dragonpay.reactive_dragonpay_service.common;

public enum LogType {
    REQUEST,
    SUCCESS,
    ERROR,
    EXCEPTION,
    RESPONSE,
    CUSTOM,
    WARN,
    DEBUG,
    INFO;

    @Override
    public String toString() {
        return name();
    }
}
