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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @see com.cathive.sass.SassFileContext
 * @author Benjamin P. Jung
 */
public class SassFileContextTest {

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
    public void testSimpleFileContext() throws Exception {
        final SassContext context = SassFileContext.create(this.simpleScssPath);
        context.compile(System.out);
    }

}
