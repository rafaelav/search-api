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

package com.mclinic.search.api.internal.provider;

import com.google.inject.Inject;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import java.io.IOException;

public class SearcherProvider implements SearchProvider<IndexSearcher> {

    private SearchProvider<IndexReader> readerProvider;

    @Inject
    protected SearcherProvider(final SearchProvider<IndexReader> readerProvider) {
        this.readerProvider = readerProvider;
    }

    @Override
    public IndexSearcher get() throws Exception {
        IndexReader indexReader = readerProvider.get();
        return new IndexSearcher(indexReader);
    }
}
