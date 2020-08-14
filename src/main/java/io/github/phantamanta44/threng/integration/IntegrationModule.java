package io.github.phantamanta44.threng.integration;

import io.github.phantamanta44.libnine.util.nullity.Reflected;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IntegrationModule {

    void init();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Register {

        @Reflected
        String value();

    }

}
