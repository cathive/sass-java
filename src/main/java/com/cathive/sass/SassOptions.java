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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import org.libsass.sassc.SassLibrary;
import org.libsass.sassc.SassLibrary.Sass_Options;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static com.google.common.collect.Lists.transform;
import static java.util.Arrays.asList;

/**
 * @author Benjamin P. Jung
 */
public class SassOptions {

    /** Underlying native options structure. */
    Sass_Options $options;

    /**
     * Default constructor.
     * <p>Creates a new set of default Sass options.</p>
     */
    protected SassOptions() {
        super();
        this.$options = SassLibrary.INSTANCE.sass_make_options();
    }

    /**
     * Creates a new set of sass options for/from the given Sass context object.
     * @param context
     *     Sass context object to retrieve the options from.
     */
    SassOptions(@Nonnull final SassContext context) {
        this.$options = SassLibrary.INSTANCE.sass_context_get_options(context.$context);
    }

    public void setPrecission(final int precission) {
        SassLibrary.INSTANCE.sass_option_set_precision(this.$options, precission);
    }

    public int getPrecission() {
        return SassLibrary.INSTANCE.sass_option_get_precision(this.$options);
    }

    public void setOutputStyle(final SassOutputStyle outputStyle) {
        SassLibrary.INSTANCE.sass_option_set_output_style(this.$options, outputStyle.getIntValue());
    }

    public SassOutputStyle getOutputStyle() {
        final int $output_style = SassLibrary.INSTANCE.sass_option_get_output_style(this.$options);
        switch ($output_style) {
            case SassLibrary.Sass_Output_Style.SASS_STYLE_NESTED:
                return SassOutputStyle.NESTED;
            case SassLibrary.Sass_Output_Style.SASS_STYLE_EXPANDED:
                return SassOutputStyle.EXPANDED;
            case SassLibrary.Sass_Output_Style.SASS_STYLE_COMPACT:
                return SassOutputStyle.COMPACT;
            case SassLibrary.Sass_Output_Style.SASS_STYLE_COMPRESSED:
                return SassOutputStyle.COMPRESSED;
            default:
                throw new IllegalStateException(MessageFormat.format("Unknown Sass output style: {0}", $output_style));
        }
    }

    public void setSourceComments(final boolean sourceComments) {
        SassLibrary.INSTANCE.sass_option_set_source_comments(this.$options, sourceComments ? (byte) 1 : (byte) 0);
    }

    public boolean getSourceComments() {
        return SassLibrary.INSTANCE.sass_option_get_source_comments(this.$options) == 1;
    }

    public void setSourceMapEmbed(final boolean sourceMapEmbed) {
        SassLibrary.INSTANCE.sass_option_set_source_map_embed(this.$options, sourceMapEmbed ? (byte) 1 : (byte) 0);
    }

    public boolean getSourceMapEmbed() {
        return SassLibrary.INSTANCE.sass_option_get_source_map_embed(this.$options) == 1;
    }

    public void setSourceMapContents(final boolean sourceMapContents) {
        SassLibrary.INSTANCE.sass_option_set_source_map_contents(this.$options, sourceMapContents ? (byte) 1 : (byte) 0);
    }

    public boolean getSourceMapContents() {
        return SassLibrary.INSTANCE.sass_option_get_source_map_contents(this.$options) == 1;
    }

    public void setOmitSourceMapUrl(final boolean omitSourceMapUrl) {
        SassLibrary.INSTANCE.sass_option_set_omit_source_map_url(this.$options, omitSourceMapUrl ? (byte) 1 : (byte) 0);
    }

    public boolean getOmitSourceMapUrl() {
        return SassLibrary.INSTANCE.sass_option_get_omit_source_map_url(this.$options) == 1;
    }

    public void setIsIndentedSyntaxSrc(final boolean isIndentedSyntaxSrc) {
        SassLibrary.INSTANCE.sass_option_set_is_indented_syntax_src(this.$options, isIndentedSyntaxSrc ? (byte) 1 : (byte) 0);
    }

    public boolean getIsIndentedSyntaxSrc() {
        return SassLibrary.INSTANCE.sass_option_get_is_indented_syntax_src(this.$options) == 1;
    }

    public void setInputPath(final Path inputPath) {
        SassLibrary.INSTANCE.sass_option_set_input_path(this.$options, inputPath.toUri().getPath());
    }

    public Path getInputPath() {
        return Paths.get(SassLibrary.INSTANCE.sass_option_get_input_path(this.$options));
    }

    public void setOutputPath(final Path outputPath) {
        SassLibrary.INSTANCE.sass_option_set_output_path(this.$options, outputPath.toUri().getPath());
    }

    public Path getOutputPath() {
        return Paths.get(SassLibrary.INSTANCE.sass_option_get_output_path(this.$options));
    }

    public void setImagePath(final Path imagePath) {
        SassLibrary.INSTANCE.sass_option_set_image_path(this.$options, imagePath.toUri().getPath());
    }

    public Path getImagePath() {
        return Paths.get(SassLibrary.INSTANCE.sass_option_get_image_path(this.$options));
    }

    public void setIncludePath(@Nonnull final Path... includePath) {
        final Collection<String> includePathAsStringArray = transform(asList(includePath), new Function<Path, String>() {
            @Nonnull
            @Override
            public String apply(final Path input) {
                return input.toUri().getPath();
            }
        });
        this.setIncludePath(includePathAsStringArray.toArray(new String[includePathAsStringArray.size()]));
    }

    public void setIncludePath(@Nonnull final String... includePath) {
        final String $include_path;
        if (includePath.length == 0) {
            $include_path = "";
        } else {
            $include_path = Joiner.on(File.pathSeparatorChar).join(includePath);
        }
        SassLibrary.INSTANCE.sass_option_set_include_path(this.$options, $include_path);
    }

    public void clearIncludePath() {
        SassLibrary.INSTANCE.sass_option_set_include_path(this.$options, (String) null);
    }

    public void setIncludePaths(@Nonnull final Collection<Path> includePath) {
        this.setIncludePath(includePath.toArray(new Path[includePath.size()]));
    }

    @Nonnull
    public Collection<Path> getIncludePath() {
        final String includePathsAsString = SassLibrary.INSTANCE.sass_option_get_include_path(this.$options);
        if (includePathsAsString == null) {
            return Collections.emptyList();
        }
        return transform(Arrays.asList(includePathsAsString.split(File.pathSeparator)), new Function<String, Path>() {
            @Nullable
            @Override
            public Path apply(final String input) {
                return Paths.get(input);
            }
        });
    }

    public void pushIncludePath(@Nonnull final Path path) {
        SassLibrary.INSTANCE.sass_option_push_include_path(this.$options, path.toUri().getPath());
    }

    public void setSourceMapFile(@Nonnull final Path sourceMapFile) {
        SassLibrary.INSTANCE.sass_option_set_source_map_file(this.$options, sourceMapFile.toUri().getPath());
    }

    public Path getSourceMapFile() {
        return Paths.get(SassLibrary.INSTANCE.sass_option_get_source_map_file(this.$options));
    }

}
