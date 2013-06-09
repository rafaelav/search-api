/**
 * Copyright 2012 Muzima Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.muzima.search.api.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.throwingproviders.ThrowingProviderBinder;
import com.muzima.search.api.internal.http.CustomKeyStore;
import com.muzima.search.api.internal.http.CustomKeyStoreProvider;
import com.muzima.search.api.internal.lucene.DefaultIndexer;
import com.muzima.search.api.internal.lucene.Indexer;
import com.muzima.search.api.internal.provider.AnalyzerProvider;
import com.muzima.search.api.internal.provider.DirectoryProvider;
import com.muzima.search.api.internal.provider.ReaderProvider;
import com.muzima.search.api.internal.provider.SearchProvider;
import com.muzima.search.api.internal.provider.SearcherProvider;
import com.muzima.search.api.internal.provider.WriterProvider;
import com.muzima.search.api.resource.Resource;
import com.muzima.search.api.service.RestAssuredService;
import com.muzima.search.api.service.impl.RestAssuredServiceImpl;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

public class SearchModule extends AbstractModule {

    /**
     * Configures a {@link com.google.inject.Binder} via the exposed methods.
     */
    @Override
    protected void configure() {
        bind(Integer.class)
                .annotatedWith(Names.named("connection.timeout"))
                .toInstance(1000);

        bind(new TypeLiteral<Map<String, Resource>>() {})
                .toInstance(new HashMap<String, Resource>());

        bind(Indexer.class)
                .to(DefaultIndexer.class)
                .in(Singleton.class);

        bind(RestAssuredService.class)
                .to(RestAssuredServiceImpl.class)
                .in(Singleton.class);

        bind(CustomKeyStore.class).toProvider(CustomKeyStoreProvider.class);

        bind(Version.class).toInstance(Version.LUCENE_36);
        bind(Analyzer.class).toProvider(AnalyzerProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, Directory.class)
                .to(DirectoryProvider.class)
                .in(Singleton.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexReader.class)
                .to(ReaderProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexSearcher.class)
                .to(SearcherProvider.class);

        ThrowingProviderBinder.create(binder())
                .bind(SearchProvider.class, IndexWriter.class)
                .to(WriterProvider.class);

        // proxy bindings to allow us to connect to the test server
        bind(Boolean.class)
                .annotatedWith(Names.named("connection.use.proxy"))
                .toInstance(Boolean.FALSE);
        bind(Proxy.Type.class)
                .annotatedWith(Names.named("connection.proxy.type"))
                .toInstance(Proxy.Type.SOCKS);
        bind(String.class)
                .annotatedWith(Names.named("connection.proxy.host"))
                .toInstance("localhost");
        bind(Integer.class)
                .annotatedWith(Names.named("connection.proxy.port"))
                .toInstance(1080);

        // custom data store bindings for self signed certificates
        bind(String.class)
                .annotatedWith(Names.named("key.store.type"))
                .toInstance("test-server-key-store-type");
        bind(String.class)
                .annotatedWith(Names.named("key.store.password"))
                .toInstance("test-server-key-store-password");
        bind(InputStream.class)
                .annotatedWith(Names.named("key.store.stream"))
                .toInstance(new ByteArrayInputStream("test-server-key-store-stream".getBytes()));
    }

    @Provides
    @Named("connection.proxy")
    Proxy createProxy(@Named("connection.use.proxy") final Boolean useProxy,
                      @Named("connection.proxy.type") final Proxy.Type proxyType,
                      @Named("connection.proxy.host") final String proxyHost,
                      @Named("connection.proxy.port") final Integer proxyPort) {
        Proxy proxy = Proxy.NO_PROXY;
        if (useProxy) {
            proxy = new Proxy(proxyType, new InetSocketAddress(proxyHost, proxyPort));
        }
        return proxy;
    }
}
