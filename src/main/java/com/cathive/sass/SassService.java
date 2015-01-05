/*
 * Copyright (C) 2014,2015 The Cat Hive Developers.
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

package com.cathive.sass;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * A service that can be used to compile .scss files.
 * @author Benjamin P. Jung
 */
@Named("sassService")
@Singleton
public class SassService {

    /**
     * Default constructor.
     * <p>Creates a new libsass service instance.</p>
     */
    public SassService() {
        super();
    }


    @PreDestroy
    protected void dispose() {
        // TODO Free all native handles correctly.
    }

}
