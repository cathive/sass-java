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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * This class contains a bunch of static inner classes that can deal with
 * the validation of SCSS files.
 * @author Benjamin P. Jung
 */
public final class ScssFileValidation {

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(ScssFileValidation.class.getName());


    public static class ScssFilePathValidator implements ConstraintValidator<ScssFile, Path> {
        @Override
        public void initialize(final ScssFile constraintAnnotation) {
            // Nothing to do.
        }
        @Override
        public boolean isValid(final Path value, final ConstraintValidatorContext context) {
            // TODO Implement more checks.
            return Files.isRegularFile(value);
        }
    }

    /** Private c-tor to avoid instantiation. */
    private ScssFileValidation() { /* No instances for you!!!1 */ }

}
