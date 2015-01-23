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

import com.cathive.sass.constraints.ScssFile;
import com.cathive.sass.jna.SassLibrary;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.text.MessageFormat.format;

/**
 * A service that can be used to compile Sass files.
 * @author Benjamin P. Jung
 */
@Named("sassService")
@Singleton
public class SassService {

    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(SassService.class.getName());

    /**
     * Properties as defined in META-INF/sass.xml
     * <p>These properties will be used to validate the version information against
     * the bundled native library being used during initialization.</p>
     */
    private Properties properties;

    /**
     * Default constructor.
     * <p>Creates a new Sass service instance.</p>
     */
    public SassService() {
        super();
    }

    /**
     * Returns the version of the underlying native libsass implementation.
     * @return
     *     The version of the underlying native libsass implementation.
     */
    public String getLibsassVersion() {
        return SassLibrary.INSTANCE.libsass_version();
    }

    /**
     * Creates a Sass file context for the given input file.
     * @param inputFile
     *     SCSS input file to be used when creating the Sass context.
     * @return
     *     A Sass context that can be used to compile the given input file.
     */
    public SassContext createContext(@NotNull @ScssFile final Path inputFile) {
        return SassFileContext.create(inputFile);
    }

    /**
     * Creates a Sass file context for the given input file.
     * @param inputFile
     *     SCSS input file to be used when creating the Sass context.
     * @return
     *     A Sass context that can be used to compile the given input file.
     */
    public SassContext createContext(@NotNull @ScssFile final String inputFile) {
        return this.createContext(Paths.get(inputFile));
    }

    @PostConstruct
    protected void initialize() throws Exception {

        final String libsassVersion = SassLibrary.INSTANCE.libsass_version();

        this.properties = new Properties();
        this.properties.loadFromXML(this.getClass().getClassLoader().getResourceAsStream("META-INF/sass.xml"));

        // Extracts the expected libsass version from the sass properties.
        final String expectedLibsassVersion = this.properties.getProperty("libsass.version", "[N/A]");

        if (!expectedLibsassVersion.equals("[N/A]") && !libsassVersion.equals(expectedLibsassVersion)) {
            throw new IllegalStateException(format("libsass version mismatch. Expected: {0}, found: {1}", libsassVersion, expectedLibsassVersion));
        }

        LOGGER.log(Level.INFO, "libsass wrapper successfully initialized. (libsass_version() -> \"{0}\")...", libsassVersion);
    }

    @PreDestroy
    protected void dispose() {
        /* Nothing to be done ... for now! */
    }

}
