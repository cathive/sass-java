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

import javax.annotation.Nonnull;
import java.text.MessageFormat;

/**
 * Exception to be used when compilation of Sass contents fails.
 * @author Benjamin P. Jung
 */
public class SassCompilationException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1L;

    /** Numeric status code as returned by the libsass compiler. */
    private final int status;

    /** Name of the input file that caused issues. */
    private final String fileName;

    /** Line number of the input that caused the compilation error. */
    private final int line;

    /** column number of the input that caused the compilation error. */
    private final int column;

    /** JSON representation of the error message. */
    private final String json;


    /**
     * Creates a new Sass compilation exception.
     * @param status
     *     Numeric status code as returned by the libsass compiler.
     * @param message
     *     Formatted humand-readable error message.
     * @param fileName
     *     Name of the input file that caused issues.
     * @param line
     *     Line number of the input that caused the compilation error.
     * @param column
     *     column number of the input that caused the compilation error.
     * @param json
     *     JSON representation of the error message.
     */
    public SassCompilationException(final int status,
                                    @Nonnull final String message,
                                    @Nonnull final String fileName,
                                    final int line,
                                    final int column,
                                    @Nonnull final String json) {

        super(MessageFormat.format("[{0}:{1}:{2}] {3}", fileName, line, column, message));

        this.status = status;
        this.fileName = fileName;
        this.line = line;
        this.column = column;
        this.json = json;

    }

    public int getStatus() {
        return this.status;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public String getJson() {
        return this.json;
    }

}
