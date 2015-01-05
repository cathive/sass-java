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

import java.text.MessageFormat;

/**
 * @author Benjamin P. Jung
 */
public class SassCompilationException extends RuntimeException {

    private final int status;
    private final String fileName;
    private final int line;
    private final int column;
    private final String json;


    public SassCompilationException(final int status, final String message, final String fileName, final int line, final int column, final String json) {
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
