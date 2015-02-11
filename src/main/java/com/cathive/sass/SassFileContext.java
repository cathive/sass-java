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

import com.cathive.sass.jna.SassLibrary;
import com.cathive.sass.jna.SassLibrary.Sass_File_Context;

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

    /**
     * Creates a new Sass file context.
     * @param $file_context
     *     Underlying native Sass_File_Context structure.
     */
    protected SassFileContext(@Nonnull Sass_File_Context $file_context) {
        super(SassLibrary.INSTANCE.sass_file_context_get_context($file_context), false);
        this.$file_context = $file_context;
    }

    public static SassFileContext create(@Nonnull final Path inputFile) {
        final Sass_File_Context $file_context = SassLibrary.INSTANCE.sass_make_file_context(inputFile.toFile().getAbsolutePath());
        return new SassFileContext($file_context);
    }


    @Override
    @Nonnull
    protected SassLibrary.Sass_Compiler createCompiler() {
        return SassLibrary.INSTANCE.sass_make_file_compiler(this.$file_context);
    }

    @Override
    public void setOptions(@Nonnull final SassOptions options) {
        super.setOptions(options);
        SassLibrary.INSTANCE.sass_file_context_set_options(this.$file_context, this.options.$options);
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.$file_context != null) {
            SassLibrary.INSTANCE.sass_delete_file_context(this.$file_context);
        }
        super.finalize();
    }

}
