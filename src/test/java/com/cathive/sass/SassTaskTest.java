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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author rick
 */
public class SassTaskTest {

    private java.nio.file.Path workingDirectory;
    private java.nio.file.Path simpleScssPath;
    private java.nio.file.Path complexScssPath;
    private java.nio.file.Path includes1Path;
    private java.nio.file.Path includes2Path;

    @Before
    public void init() throws Exception {

        this.workingDirectory = Files.createTempDirectory("sass-java");
        this.simpleScssPath = this.workingDirectory.resolve("simple.scss");
        this.complexScssPath = this.workingDirectory.resolve("complex.scss");
        this.includes1Path = this.workingDirectory.resolve("includes1");
        this.includes2Path = this.workingDirectory.resolve("includes2");

        // Copies all the stuff that is needed for our tests to the temporary directory.
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("simple.scss"), this.simpleScssPath);
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("complex.scss"), this.complexScssPath);


        Files.createDirectories(includes1Path);
        Files.createDirectories(includes2Path);
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes1/_variables1.scss"), includes1Path.resolve("_variables1.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes1/_common.scss"), includes1Path.resolve("_common.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes2/_variables2.scss"), includes2Path.resolve("_variables2.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes2/_common.scss"), includes2Path.resolve("_common.scss"));

    }

    @After
    public void shutdown() throws Exception {
        Files.walkFileTree(this.workingDirectory, new SimpleFileVisitor<java.nio.file.Path>() {
            @Override
            public FileVisitResult visitFile(java.nio.file.Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(java.nio.file.Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void testExecute() {
        SassTask instance = new SassTask();
        instance.addIncludePaths(new String[]{this.includes1Path.toString(), this.includes2Path.toString()});
        instance.setOutdir(this.workingDirectory.toString());
        instance.setIn(this.complexScssPath.toString());
        instance.setOutputstyle(0);
        instance.execute();
        java.nio.file.Path expected = this.workingDirectory.resolve("complex.css");
        Assert.assertTrue(expected.toFile().exists());
    }

}
