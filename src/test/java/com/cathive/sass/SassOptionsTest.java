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


        // Precision
        options.setPrecision(0);
        assertEquals(0, options.getPrecision());
        options.setPrecision(10);
        assertEquals(10, options.getPrecision());

        // Output style
        options.setOutputStyle(SassOutputStyle.NESTED);
        assertEquals(SassOutputStyle.NESTED, options.getOutputStyle());
        options.setOutputStyle(SassOutputStyle.EXPANDED);
        assertEquals(SassOutputStyle.EXPANDED, options.getOutputStyle());
        options.setOutputStyle(SassOutputStyle.COMPRESSED);
        assertEquals(SassOutputStyle.COMPRESSED, options.getOutputStyle());
        options.setOutputStyle(SassOutputStyle.COMPACT);
        assertEquals(SassOutputStyle.COMPACT, options.getOutputStyle());

        // Source comments
        options.setSourceComments(false);
        assertEquals(false, options.getSourceComments());
        options.setSourceComments(true);
        assertEquals(true, options.getSourceComments());

        // Source map embed
        options.setSourceMapEmbed(false);
        assertEquals(false, options.getSourceMapEmbed());
        options.setSourceMapEmbed(true);
        assertEquals(true, options.getSourceMapEmbed());

        // Source map contents
        options.setSourceMapContents(false);
        assertEquals(false, options.getSourceMapContents());
        options.setSourceMapContents(true);
        assertEquals(true, options.getSourceMapContents());

        // Omit source map URL
        options.setOmitSourceMapUrl(false);
        assertEquals(false, options.getOmitSourceMapUrl());
        options.setOmitSourceMapUrl(true);
        assertEquals(true, options.getOmitSourceMapUrl());

        // Is indented syntax src
        options.setIsIndentedSyntaxSrc(false);
        assertEquals(false, options.getIsIndentedSyntaxSrc());
        options.setIsIndentedSyntaxSrc(true);
        assertEquals(true, options.getIsIndentedSyntaxSrc());

        // Input path
        options.setInputPath(Paths.get("/my/path1"));
        assertEquals(Paths.get("/my/path1").toAbsolutePath(), options.getInputPath().toAbsolutePath());
        options.setInputPath(Paths.get("/my/other/path2"));
        assertEquals(Paths.get("/my", "other", "path2").toAbsolutePath(), options.getInputPath().toAbsolutePath());

        // Output path
        options.setOutputPath(Paths.get("/my/path1"));
        assertEquals(Paths.get("/my/path1").toAbsolutePath(), options.getOutputPath().toAbsolutePath());
        options.setOutputPath(Paths.get("/my/other/path2"));
        assertEquals(Paths.get("/my", "other", "path2").toAbsolutePath(), options.getOutputPath().toAbsolutePath());

        // includePath functionality
        assertEquals(0, options.getIncludePath().size());
        options.setIncludePath("/path1", "/path2");
        assertTrue(options.getIncludePath().contains(Paths.get("/path1")));
        assertTrue(options.getIncludePath().contains(Paths.get("/path2")));
        assertEquals(2, options.getIncludePath().size());
        options.pushIncludePath("/path3");
        assertEquals(3, options.getIncludePath().size());
        assertTrue(options.getIncludePath().contains(Paths.get("/path3").toAbsolutePath()));
        options.clearIncludePath();
        assertEquals(0, options.getIncludePath().size());

        // Source map file
        options.setSourceMapFile("/tmp/source.map");
    }

}
