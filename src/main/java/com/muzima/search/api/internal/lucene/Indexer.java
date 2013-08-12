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

package com.muzima.search.api.internal.lucene;

import com.muzima.search.api.model.object.Searchable;
import com.muzima.search.api.resource.Resource;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Indexer {

    List<Searchable> loadObjects(final Resource resource, final InputStream inputStream) throws Exception;


    <T> T getObject(final String key, final Class<T> clazz) throws Exception;

    Searchable getObject(final String key, final Resource resource) throws Exception;

    <T> List<T> getObjects(final Query query, final Class<T> clazz) throws Exception;

    List<Searchable> getObjects(final Query query, final Resource resource) throws Exception;

    <T> List<T> getObjects(final String searchString, final Class<T> clazz) throws Exception;

    List<Searchable> getObjects(final String searchString, final Resource resource) throws Exception;

    void deleteObjects(final List<Searchable> objects, final Resource resource) throws Exception;

    void createObjects(final List<Searchable> objects, Resource resource) throws Exception;

    void updateObjects(final List<Searchable> objects, Resource resource) throws Exception;


    void commit() throws Exception;
}
