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

package com.cathive.sass.jna;

import com.cathive.sass.jna.SassLibrary;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.reader.CSSReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static com.cathive.sass.jna.SassLibrary.*;
import static org.junit.Assert.*;

/**
 * Test cases for the wrapped native library.
 * <p>The complete API definition of libsass can be found here:
 * <a href="https://github.com/sass/libsass/wiki/API-Documentation">https://github.com/sass/libsass/wiki/API-Documentation</a>.</p>
 * @author Benjamin P. Jung
 */
public class SassLibraryTest {

    private Path workingDirectory;
    private Path simpleScssPath;

    @Before
    public void init() throws Exception {

        this.workingDirectory = Files.createTempDirectory("sass-java");
        this.simpleScssPath = this.workingDirectory.resolve("simple.scss");

        // Copies all the stuff that is needed for our tests to the temporary directory.
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("simple.scss"), this.simpleScssPath);

    }

    @After
    public void shutdown() throws Exception {
        Files.walkFileTree(this.workingDirectory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void testVersion() {
        assertEquals("SassC Version mismatch", "3.1.0", SassLibrary.INSTANCE.libsass_version());
    }

    @Test
    public void testSimpleScssFile() {

        // Creates the file context and sets the precision option.
        final Sass_File_Context fileContext = SassLibrary.INSTANCE.sass_make_file_context(this.simpleScssPath.toFile().getAbsolutePath());
        final Sass_Context context = SassLibrary.INSTANCE.sass_file_context_get_context(fileContext);
        final Sass_Options contextOptions = SassLibrary.INSTANCE.sass_context_get_options(context);
        SassLibrary.INSTANCE.sass_option_set_precision(contextOptions, 10);

        // Performs the actual compilation.
        final int compilerStatus = SassLibrary.INSTANCE.sass_compile_file_context(fileContext);

        // Retrieves the CSS output string.
        final String outputString;

        try {
            if (compilerStatus != 0) {
                outputString = null;
                final String errorMessage = SassLibrary.INSTANCE.sass_context_get_error_message(context);
                fail(errorMessage);
            } else {
                outputString = SassLibrary.INSTANCE.sass_context_get_output_string(context);
            }
        } finally {
            SassLibrary.INSTANCE.sass_delete_file_context(fileContext);
        }

        // Asserts, that the creates cascading style sheet can be read and parsed.
        final CascadingStyleSheet styleSheet = CSSReader.readFromString(outputString, ECSSVersion.LATEST);
        assertNotNull(styleSheet);

    }

    @Ignore("The Sass data context seems to be defunct right now. :-(")
    @Test
    public void testSimpleScssData() {
        final Sass_Data_Context dataContext = SassLibrary.INSTANCE.sass_make_data_context(ByteBuffer.wrap("html { background-color: red; }".getBytes(Charset.forName("UTF-8"))));
        final Sass_Context context = SassLibrary.INSTANCE.sass_data_context_get_context(dataContext);
        final Sass_Options options = SassLibrary.INSTANCE.sass_data_context_get_options(dataContext);
        SassLibrary.INSTANCE.sass_option_set_source_comments(options, (byte) 0);
        SassLibrary.INSTANCE.sass_option_set_omit_source_map_url(options, (byte) 0);
        SassLibrary.INSTANCE.sass_option_set_input_path(options, "/tmp/input/styles.scss");
        SassLibrary.INSTANCE.sass_option_set_output_path(options, "/tmp/output");
        SassLibrary.INSTANCE.sass_option_set_include_path(options, "/tmp/input");
        SassLibrary.INSTANCE.sass_option_set_image_path(options, "/tmp/input");
        SassLibrary.INSTANCE.sass_option_set_precision(options, 10);
        SassLibrary.INSTANCE.sass_option_set_source_comments(options, (byte) 1);
        SassLibrary.INSTANCE.sass_option_set_output_style(options, Sass_Output_Style.SASS_STYLE_NESTED);
        SassLibrary.INSTANCE.sass_data_context_set_options(dataContext, options);
        final int compilerStatus = SassLibrary.INSTANCE.sass_compile_data_context(dataContext);
        try {
            if (compilerStatus != 0) {
                final String errorMessage = SassLibrary.INSTANCE.sass_context_get_error_message(context);
                fail(errorMessage);

            }
        } finally {
            SassLibrary.INSTANCE.sass_delete_data_context(dataContext);
        }
    }

}
