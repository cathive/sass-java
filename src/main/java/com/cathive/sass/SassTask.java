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

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import static java.text.MessageFormat.format;

/**
 * Implements an Ant task that can be used to invoke SassJava from an Ant build.
 */
public class SassTask extends Task {

    private static final String OUTPUT_EXTENSION = ".css";
    private Integer precision = null;
    private SassOutputStyle outputStyle = null;
    private Boolean sourceComments = null;
    private Boolean sourceMapEmbed = null;
    private Boolean sourceMapContents = null;
    private Boolean omitSourceMapUrl = null;
    private Boolean isIndentedSyntaxSrc = null;
    private String sourceMapFile = null;
    private String sourceMapRoot = null;
    private File outputPath = null;
    private File in = null;
    private final Collection<org.apache.tools.ant.types.Path> paths = new ArrayList<>();
    private String extension = ".scss";

    /**
     * Set the output directory where the compiled css will be placed.
     *
     * @param outputPath root directory that will contain the output.
     */
    public void setOutdir(final String outputPath) {
        if (outputPath != null && !outputPath.trim().isEmpty()) {
            this.outputPath = new File(outputPath);
        }
    }

    /**
     * Set the input scss file/s to compile.
     *
     * @param in Either a directory that contains scss files or a single scss file.
     */
    public void setIn(final String in) {
        if (in != null) {
            this.in = new File(in);
        }
    }

    public void setPrecision(final int precision) {
        this.precision = precision;
    }

    /**
     * See <a href="http://sass-lang.com/documentation/file.SASS_REFERENCE.html#style-option">http://sass-lang.com/documentation/file.SASS_REFERENCE.html#style-option</a>
     * for further information.
     * @param outputStyle 0 = nested, 1 = expanded, 2 = compact, 3 = compressed
     */
    public void setOutputstyle(final int outputStyle) {
        for (final SassOutputStyle sassOutputStyle : SassOutputStyle.values()) {
            if (sassOutputStyle.getIntValue() == outputStyle) {
                this.outputStyle = sassOutputStyle;
                break;
            }
        }
    }

    /**
     * See <a href="http://sass-lang.com/documentation/file.SASS_REFERENCE.html#line_numbers-option">http://sass-lang.com/documentation/file.SASS_REFERENCE.html#line_numbers-option</a>
     * for further information.
     * @param sourceComments true to turn on line comments.
     */
    public void setSourcecomments(final boolean sourceComments) {
        this.sourceComments = sourceComments;
    }

    public void setSourcemapembed(final boolean sourceMapEmbed) {
        this.sourceMapEmbed = sourceMapEmbed;
    }

    public void setSourcemapcontents(final boolean sourceMapContents) {
        this.sourceMapContents = sourceMapContents;
    }

    public void setOmitsourcemapurl(final boolean omitSourceMapUrl) {
        this.omitSourceMapUrl = omitSourceMapUrl;
    }

    public void setIsindentedsyntaxsrc(final boolean isIndentedSyntaxSrc) {
        this.isIndentedSyntaxSrc = isIndentedSyntaxSrc;
    }

    public void setSourcemapfile(final String sourceMapFile) {
        this.sourceMapFile = sourceMapFile;
    }

    public void setSourcemaproot(final String sourceMapRoot) {
        this.sourceMapRoot = sourceMapRoot;
    }

    public void setExtension(final String extension) {
        if (extension != null) {
            if (!extension.startsWith(".")) {
                this.extension = "." + extension;
            } else {
                this.extension = extension;
            }
        }
    }

    /**
     * Add a path which references one or more sass include directories.
     *
     * @param path The path to add.
     */
    public void addPath(final org.apache.tools.ant.types.Path path) {
        paths.add(path);
    }

    /**
     * For the given sass input file create an OutputStream to which we will write the result of the
     * Sass compilation.
     *
     * @param inputFile A sass input file to compile.
     * @return An output stream for the compiler to write to.
     */
    private OutputStream getOutput(final File inputFile) {
        OutputStream result = null;
        if (outputPath != null) {
            try {
                if (!outputPath.exists()) {
                    if (!outputPath.mkdirs()) {
                        throw new BuildException(format("Could not create output path: {0}", outputPath.getCanonicalPath()));
                    }
                }
                String filename = inputFile.getName();
                if (filename.indexOf(".") > 0) {
                    filename = filename.substring(0, filename.lastIndexOf("."));
                }
                filename += OUTPUT_EXTENSION;
                final Path output = outputPath.toPath().resolve(filename);
                final File outputFile = output.toFile();
                if (!outputFile.exists()) {
                    result = new FileOutputStream(outputFile);
                } else {
                    this.log(format("File already exists: {0} ", outputFile.getCanonicalPath()));
                }
            } catch (final IOException ex) {
                throw new BuildException(ex);
            }
        } else {
            throw new BuildException("outdir must be set");
        }
        return result;
    }

    /**
     * Gets the include directories.
     *
     * @return A collection of include directories.
     */
    private Collection<Path> getIncludeDirs() {
        final Collection<Path> includes = new HashSet<>();
        for (org.apache.tools.ant.types.Path path : paths) {
            final String[] pathElements = path.list();
            for (String pathElement : pathElements) {
                includes.add(Paths.get(pathElement));
            }
        }
        return includes;
    }

    /**
     * Get the sass files to compile.
     *
     * @return An array of files to compile.
     */
    private File[] getInputFiles() {
        File[] files = null;
        if (in != null) {
            if (in.exists()) {
                if (in.isDirectory()) {
                    files = in.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(final File dir, final String name) {
                            return name.endsWith(extension);
                        }
                    });
                } else {
                    files = new File[]{ in };
                }
            } else {
                throw new BuildException(format("Cannot find \"{0}\".", in.getAbsolutePath()));
            }
        } else {
            throw new BuildException("\"in\" must be set");
        }
        return files;
    }

    /**
     * Takes the options passed in from Ant and sets them on the SassOptions instance.
     *
     * @param options
     *   Sass options to be used.
     */
    private void setOptions(final SassOptions options) {

        if (!paths.isEmpty()) {
            options.setIncludePath(getIncludeDirs());
        }
        if (precision != null) {
            options.setPrecision(precision);
        }
        if (outputStyle != null) {
            options.setOutputStyle(outputStyle);
        }
        if (sourceComments != null) {
            options.setSourceComments(sourceComments);
        }
        if (sourceMapEmbed != null) {
            options.setSourceMapEmbed(sourceMapEmbed);
        }
        if (sourceMapContents != null) {
            options.setSourceMapContents(sourceMapContents);
        }
        if (omitSourceMapUrl != null) {
            options.setOmitSourceMapUrl(omitSourceMapUrl);
        }
        if (sourceMapFile != null) {
            options.setSourceMapFile(sourceMapFile);
        }
        if (sourceMapRoot != null) {
            options.setSourceMapRoot(sourceMapRoot);
        }
        if (isIndentedSyntaxSrc != null) {
            options.setIsIndentedSyntaxSrc(isIndentedSyntaxSrc);
        }
    }

    @Override
    public void execute() throws BuildException {
        final File[] inputFiles = getInputFiles();
        for (final File inputFile : inputFiles) {
            if (inputFile.exists()) {
                if (inputFile.canRead()) {
                    final SassContext context = SassFileContext.create(inputFile.toPath());
                    this.setOptions(context.getOptions());
                    try {
                        try (final OutputStream outputStream = getOutput(inputFile)) {
                            if (outputStream != null) {
                                this.log(format("Compiling \"{0}\"...", inputFile.getCanonicalPath()));
                                context.compile(outputStream);
                            }
                        }
                    } catch (final SassCompilationException | IOException ex) {
                        throw new BuildException(ex);
                    }
                } else {
                    throw new BuildException(format("Could not read \"{0}\".", inputFile.getAbsolutePath()));
                }
            } else {
                throw new BuildException(format("Could not find \"{0}\".", inputFile.getAbsolutePath()));
            }
        }
    }

}
