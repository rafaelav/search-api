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
import com.google.inject.name.Named;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.transform.CompressedIndexDirectory;
import org.apache.lucene.store.transform.TransformedDirectory;
import org.apache.lucene.store.transform.algorithm.ReadPipeTransformer;
import org.apache.lucene.store.transform.algorithm.StorePipeTransformer;
import org.apache.lucene.store.transform.algorithm.compress.DeflateDataTransformer;
import org.apache.lucene.store.transform.algorithm.compress.InflateDataTransformer;
import org.apache.lucene.store.transform.algorithm.security.DataDecryptor;
import org.apache.lucene.store.transform.algorithm.security.DataEncryptor;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;

public class DirectoryProvider implements SearchProvider<Directory> {

    private final String directory;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DirectoryProvider.class);

    @Inject(optional = true)
    @Named("configuration.lucene.usingCompression")
    Boolean usingCompression;

    @Inject(optional = true)
    @Named("configuration.lucene.usingEncryption")
    Boolean usingEncryption;

    @Inject(optional = true)
    @Named("configuration.lucene.password")
    String password;

    @Inject(optional = true)
    @Named("configuration.lucene.encryption")
    String encryption;

    // TODO: create a factory to customize the type of directory returned by this provider
    @Inject
    protected DirectoryProvider(final @Named("configuration.lucene.directory") String directory) {
        this.directory = directory;
    }

    @Override
    public Directory get() throws Exception {
        Directory directory = FSDirectory.open(new File(this.directory));

        if(usingEncryption) {
            byte[] salt = new byte[16];

            if (logger.isDebugEnabled()){
                logger.debug("Used password with inject - " + password);
                logger.debug("Used encryption with inject - " + encryption);
            }

            DataEncryptor enc = new DataEncryptor(encryption, password, salt, 128, false);
            DataDecryptor dec = new DataDecryptor(password, salt, false);

            if(usingCompression) {
                StorePipeTransformer st = new StorePipeTransformer(new DeflateDataTransformer(Deflater.BEST_COMPRESSION, 1), enc);
                ReadPipeTransformer rt = new ReadPipeTransformer(dec, new InflateDataTransformer());

                if(logger.isDebugEnabled())
                    logger.debug("Encryption + compression");

                // encrypted and compressed
                return new TransformedDirectory(directory, st, rt);
            }

            // encrypted but not compressed
            if(logger.isDebugEnabled())
                logger.debug("Encryption");
            return new TransformedDirectory(directory, enc, dec);
        }
        else {
            if(usingCompression) {
                // not encrypted but compressed
                if(logger.isDebugEnabled())
                    logger.debug("Compression");
                return new CompressedIndexDirectory(directory);
            }
        }

        // not encrypted not compressed
        if(logger.isDebugEnabled())
            logger.debug("Normal directory");
        return directory;
    }
}
