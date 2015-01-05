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

import org.libsass.sassc.SassLibrary;
import org.libsass.sassc.SassLibrary.Sass_File_Context;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author Benjamin P. Jung
 */
public class SassFileContext extends SassContext {

    /** Underlying native Sass file context. */
    protected Sass_File_Context $file_context;

    protected SassFileContext(@Nonnull Sass_File_Context $file_context) {
        super(SassLibrary.INSTANCE.sass_file_context_get_context($file_context));
        this.$file_context = $file_context;
    }

    public static SassFileContext create(@Nonnull final Path inputFile) {
        final Sass_File_Context $file_context = SassLibrary.INSTANCE.sass_make_file_context(inputFile.toUri().getPath());
        return new SassFileContext($file_context);
    }

    @Override
    public void compile(@WillNotClose @Nonnull OutputStream outputStream) throws SassCompilationException, IOException {

        // Creates a SCSS compiler instance and compiles the SCSS input.
        final SassLibrary.Sass_Compiler $compiler = SassLibrary.INSTANCE.sass_make_file_compiler(this.$file_context);
        final int parseStatus = SassLibrary.INSTANCE.sass_compiler_parse($compiler);
        final int compileStatus = SassLibrary.INSTANCE.sass_compiler_execute($compiler);
        final String output = SassLibrary.INSTANCE.sass_context_get_output_string(this.$context);

        // Deletes the underlying native compiler object and releases allocated memory.
        SassLibrary.INSTANCE.sass_delete_compiler($compiler);

        // Error handling.
        if (parseStatus != 0) { this.throwCompilationException(parseStatus); }
        if (compileStatus != 0) { this.throwCompilationException(compileStatus); }

        // Writes the result to the output stream.
        outputStream.write(output.getBytes());

    }

    @Override
    public void setOptions(@Nonnull final SassOptions options) {
        super.setOptions(options);
        SassLibrary.INSTANCE.sass_file_context_set_options(this.$file_context, this.options.$options);
    }

    @Override
    public void finalize() throws Throwable {
        if (this.$file_context != null) {
            SassLibrary.INSTANCE.sass_delete_file_context(this.$file_context);
        }
        super.finalize();
    }

}
