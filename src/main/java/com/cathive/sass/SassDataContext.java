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

import com.google.common.io.ByteStreams;
import com.cathive.sass.jna.SassLibrary;
import com.cathive.sass.jna.SassLibrary.Sass_Compiler;
import com.cathive.sass.jna.SassLibrary.Sass_Data_Context;

import javax.annotation.Nonnull;
import javax.annotation.WillClose;
import javax.annotation.WillNotClose;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.logging.Logger;

/**
 * @author Benjamin P. Jung
 */
public class SassDataContext extends SassContext {

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(SassDataContext.class.getName());

    protected Sass_Data_Context $data_context;


    protected SassDataContext(@Nonnull final Sass_Data_Context $data_context) {
        super(SassLibrary.INSTANCE.sass_data_context_get_context($data_context), false);
        this.$data_context = $data_context;
    }

    /**
     * Creates a new Sass context object from the given input stream.
     * The input stream must produce a valid SCSS document when read.
     * @param inputStream
     *     Input stream to be read. Must produce a valid SCSS document when read.
     * @return
     *     A new Sass data context.
     * @throws IOException
     *     If anything weird happens while processing the given input stream.
     */
    public static SassDataContext create(@WillClose @Nonnull final InputStream inputStream) throws IOException {
        final SassDataContext dataContext;
        try {
            final byte[] sourceBytes = ByteStreams.toByteArray(inputStream);
            final ByteBuffer sourceString = ByteBuffer.wrap(sourceBytes);
            dataContext = new SassDataContext(SassLibrary.INSTANCE.sass_make_data_context(sourceString));
        } finally {
            inputStream.close();
        }
        return dataContext;
    }

    public static SassDataContext create(@Nonnull final String inputString) throws IOException {
        return create(new ByteArrayInputStream(inputString.getBytes(Charset.forName("UTF-8"))));
    }

    @Override
    public void setOptions(@Nonnull final SassOptions options) {
        super.setOptions(options);
        SassLibrary.INSTANCE.sass_data_context_set_options(this.$data_context, this.options.$options);
    }

    @Override
    @Nonnull
    protected Sass_Compiler createCompiler() {
        return SassLibrary.INSTANCE.sass_make_data_compiler(this.$data_context);
    }

    @Override
    protected void finalize() throws Throwable {
        if (this.$data_context != null) {
            SassLibrary.INSTANCE.sass_delete_data_context(this.$data_context);
        }
        super.finalize();
    }

}
