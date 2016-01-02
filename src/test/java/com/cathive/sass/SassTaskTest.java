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
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildFileRule;
import org.junit.After;
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @see com.cathive.sass.SassTask
 */
public class SassTaskTest {

    private Path workingDirectory;
    private Path simpleScssPath;
    private Path complexScssPath;
    private Path includes1Path;
    private Path includes2Path;
    private Path buildFilePath;

    @Rule
    public final BuildFileRule buildRule = new BuildFileRule();

    @Before
    public void setUp() throws Exception {
        this.workingDirectory = Files.createTempDirectory("sass-java");
        this.simpleScssPath = this.workingDirectory.resolve("simple.scss");
        this.complexScssPath = this.workingDirectory.resolve("complex.scss");
        this.includes1Path = this.workingDirectory.resolve("includes1");
        this.includes2Path = this.workingDirectory.resolve("includes2");
        this.buildFilePath = this.workingDirectory.resolve("build.xml");

        Properties props = System.getProperties();
        props.setProperty("sass-java.test.workingdir", this.workingDirectory.toString());

        // Copies all the stuff that is needed for our tests to the temporary directory.
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("simple.scss"), this.simpleScssPath);
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("complex.scss"), this.complexScssPath);

        Files.createDirectories(includes1Path);
        Files.createDirectories(includes2Path);
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes1/_variables1.scss"), includes1Path.resolve("_variables1.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes1/_common.scss"), includes1Path.resolve("_common.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes2/_variables2.scss"), includes2Path.resolve("_variables2.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("includes2/_common.scss"), includes2Path.resolve("_common.scss"));
        Files.copy(this.getClass().getClassLoader().getResourceAsStream("build.xml"), this.buildFilePath);
        buildRule.configureProject(this.buildFilePath.toString());
    }

    @Test
    public void testExecute() {
        testTask("test");
    }

    @Test(expected = BuildException.class)
    public void testExecuteMissingIn() {
        try {
            buildRule.executeTarget("testInMissing");
            fail("BuildException should have been thrown");
        } catch (final BuildException ex) {
            Assert.assertEquals("\"in\" must be set", ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void testWithPrecision() {
        testTask("testWithPrecision");
    }

    @Test
    public void testWithCompressedOutputstyle() {
        testTask("testWithCompressedOutputstyle");
    }

    @Test
    public void testWithSourcecomments() {
        testTask("testWithSourcecomments");
    }

    @Test
    public void testWithSourcemapembed() {
        testTask("testWithSourcemapembed");
    }

    @Test
    public void testWithSourcemapcontents() {
        testTask("testWithSourcemapcontents");
    }

    @Test
    public void testWithomitSourcemapurl() {
        testTask("testWithomitSourcemapurl");
    }

    @Test
    public void testWithSourcemapfile() {
        testTask("testWithSourcemapfile");
    }

    @Test
    public void testWithSourcemaproot() {
        testTask("testWithSourcemaproot");
    }

    /**
     * A helper for basic testing of Ant targets that expect to succeed.
     * It is expected that the Ant target performs a `clean` before running.
     */
    private void testTask(final String targetName) {
        buildRule.executeTarget(targetName);
        final Path outputPath = this.workingDirectory.resolve("output");
        final Path expectedComplex = outputPath.resolve("complex.css");
        final Path expectedSimple = outputPath.resolve("simple.css");
        assertTrue(expectedComplex.toFile().exists());
        assertTrue(expectedSimple.toFile().exists());
    }
}
