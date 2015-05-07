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

import org.junit.Ignore;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @see com.cathive.sass.SassDataContext
 * @author Benjamin P. Jung
 */
public class SassDataContextTest {

    @Ignore("The Sass data context seems to be defunct right now. :-(")
    @Test
    public void testCompiler() throws Exception {
        final SassDataContext context = SassDataContext.create("div { background-color: red; }");
        context.getOptions().setPrecission(10);
        //context.getOptions().setSourceComments(true);
        //context.getOptions().setInputPath(Paths.get("/tmp", "simple.scss"));
        //context.getOptions().setOutputPath(Paths.get("/tmp"));
        context.compile(System.out);
    }

}
