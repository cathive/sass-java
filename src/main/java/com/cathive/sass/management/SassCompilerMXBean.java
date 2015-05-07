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

package com.cathive.sass.management;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.management.MXBean;
import java.io.IOException;

/**
 * @author Benjamin P. Jung
 */
@MXBean
public interface SassCompilerMXBean {

    /**
     * Returns the version of the underlying native libsass implementation.
     * @return
     *     The version of the underlying native libsass implementation.
     */
    String getLibsassVersion();

    /**
     * Compiles a given SCSS input file.
     * @param inputPath
     *     Path to the input file.
     * @param outputPath
     *     Path to the output file. Can be {@code null} to skip the output
     *     file creation. (The result will just be returned by this method then).
     * @param includePath
     *     All include paths that shall be searched when using {@code @import} statements
     *     inside your SCSS files.
     * @return
     *     The compiler output.
     * @throws com.cathive.sass.SassCompilationException
     *     If compilation fails.
     * @throws java.io.IOException
     *     If reading of the input file(s) or writing of the output file
     *     fails.
     */
    String compile(@Nonnull String inputPath, @Nullable String outputPath, @Nullable String[] includePath) throws IOException;

}
