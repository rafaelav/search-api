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

package com.mclinic.search.api.model.object;

import com.mclinic.search.api.Loggable;

/**
 * Top level object for the search api. All object must implement this interface.
 */
public interface Searchable extends Loggable {

    /**
     * Get the checksum for the searchable object.
     *
     * @return the searchable object's checksum.
     */
    String getChecksum();

    /**
     * Set the checksum for the searchable object.
     *
     * @param checksum the checksum for the searchable object.
     */
    void setChecksum(final String checksum);

}