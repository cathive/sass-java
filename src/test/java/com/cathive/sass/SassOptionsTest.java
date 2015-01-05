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

import org.junit.Test;
import org.libsass.sassc.SassLibrary;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @see com.cathive.sass.SassOptions
 * @author Benjamin P. Jung
 */
public class SassOptionsTest {

    @Test
    public void testOptionsWithoutContext() throws Exception {

        final SassOptions options = new SassOptions();



        // includePath functionality
        assertEquals(0, options.getIncludePath().size());
        options.setIncludePath("/path1", "/path2");
        assertTrue(options.getIncludePath().contains(Paths.get("/path1")));
        assertTrue(options.getIncludePath().contains(Paths.get("/path2")));
        assertEquals(2, options.getIncludePath().size());
        options.clearIncludePath();
        assertEquals(0, options.getIncludePath().size());

    }

}
