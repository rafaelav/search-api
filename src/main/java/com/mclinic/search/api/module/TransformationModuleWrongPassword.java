package com.mclinic.search.api.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class TransformationModuleWrongPassword extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("configuration.lucene.password")).toInstance("lucenetransform-wrong");
        bind(String.class).annotatedWith(Names.named("configuration.lucene.encryption")).toInstance("AES/ECB/PKCS5Padding");
        bind(Boolean.class).annotatedWith(Names.named("configuration.lucene.usingEncryption")).toInstance(true);
        bind(Boolean.class).annotatedWith(Names.named("configuration.lucene.usingCompression")).toInstance(false);
    }
}
