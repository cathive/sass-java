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
import com.cathive.sass.jna.SassLibrary.Sass_Context;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An (abstract) Sass context definition.
 * <p>A Sass context can be obtained either by passing a file or a SCSS string definition.</p>
 * @see com.cathive.sass.SassFileContext#create(java.nio.file.Path)
 * @see com.cathive.sass.SassDataContext#create(java.io.InputStream)
 * @author Benjamin P. Jung
 */
public abstract class SassContext {

    /** This flag determines whether this is a standalone Sass context instance. */
    private boolean standalone;

    /** Underlying native Sass context. */
    protected Sass_Context $context;

    /** Underlying native Sass options associated with the data or file context. */
    protected SassOptions options;

    /**
     * Creates a new Sass context wrapper instance.
     * @param $context
     *     Native underlying Sass context.
     * @param standalone
     *     Whether this context is managed by a data or file context
     *     or not. (Unmanaged / standalone context must dispose their native
     *     components during the Object's finalize phase).
     */
    protected SassContext(@Nonnull final Sass_Context $context, boolean standalone) {
        super();
        this.$context = $context;
        this.options = new SassOptions(this);
    }

    /**
     * Performs compilation of the SCSS data that is represented by this Sass context.
     * @param outputStream
     *     Output stream to be written to.
     * @throws SassCompilationException
     *     If compilation of the SCSS source file / data fails.
     */
    public abstract void compile(@WillNotClose @Nonnull final OutputStream outputStream) throws SassCompilationException, IOException;

    /**
     * Returns the options associated with this Sass context.
     * @return
     *     Options associated with this Sass context.
     */
    @Nonnull
    public SassOptions getOptions() {
        return this.options;
    }

    public void setOptions(@Nonnull final SassOptions options) {
        this.options = options;
    }

    /**
     * Throws an exception if the compilation of SCSS data or an SCSS file has failed.
     * @param compileStatus
     *     The numeric status code that has been reported by the SCSS compiler
     *     instance.
     */
    protected void throwCompilationException(final int compileStatus) {
        throw new SassCompilationException(
                compileStatus,
                SassLibrary.INSTANCE.sass_context_get_error_message(this.$context),
                SassLibrary.INSTANCE.sass_context_get_error_file(this.$context),
                SassLibrary.INSTANCE.sass_context_get_error_line(this.$context).intValue(),
                SassLibrary.INSTANCE.sass_context_get_error_column(this.$context).intValue(),
                SassLibrary.INSTANCE.sass_context_get_error_json(this.$context)
        );
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.standalone && this.$context != null) {
            // TODO Free the native memory.
        }
        super.finalize();
    }

}
