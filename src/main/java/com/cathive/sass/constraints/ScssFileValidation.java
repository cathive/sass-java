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

package com.cathive.sass.constraints;

import javax.annotation.Nonnull;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a bunch of static inner classes that can deal with
 * the validation of SCSS files.
 * @author Benjamin P. Jung
 */
public final class ScssFileValidation {

    /**
     * Storage of all the allowed file extension that can be used for
     * SCSS file content.
     */
    private static final Set<String> ALLOWED_FILE_EXTENSIONS;
    static {
        ALLOWED_FILE_EXTENSIONS = new HashSet<>();
        ALLOWED_FILE_EXTENSIONS.add(".sass");
        ALLOWED_FILE_EXTENSIONS.add(".scss");
    }

    /**
     * Returns all valid file extensions that can be used for SCSS files.
     * @return
     *     All valid file extensions that can be used for SCSS files.
     */
    public static Set<String> getAllowedFileExtensions() {
        return Collections.unmodifiableSet(ALLOWED_FILE_EXTENSIONS);
    }

    /**
     * Changes the contents of the file extensions that can be used for SCSS files.
     * @param allowedFileExtensions
     *     File extensions that can be used for SCSS files.
     */
    public static void setAllowedFileExtensions(@Nonnull final Set<String> allowedFileExtensions) {
        ALLOWED_FILE_EXTENSIONS.clear();
        ALLOWED_FILE_EXTENSIONS.addAll(allowedFileExtensions);
    }


    public static class ScssFileFileValidator implements ConstraintValidator<ScssFile, File> {
        @Override
        public void initialize(final ScssFile constraintAnnotation) { /* Nothing to do. */ }

        @Override
        public boolean isValid(final File value, final ConstraintValidatorContext context) {
            // TODO Implement more checks.
            return value.isFile();
        }
    }


    public static class ScssFilePathValidator implements ConstraintValidator<ScssFile, Path> {
        @Override
        public void initialize(final ScssFile constraintAnnotation) { /* Nothing to do. */ }

        @Override
        public boolean isValid(final Path value, final ConstraintValidatorContext context) {
            // TODO Implement more checks.
            return Files.isRegularFile(value);
        }
    }


    /** Private c-tor to avoid instantiation. */
    private ScssFileValidation() { /* No instances for you!!!1 */ }

}
