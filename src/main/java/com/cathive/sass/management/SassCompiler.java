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

package com.cathive.sass.management;

import com.cathive.sass.SassContext;
import com.cathive.sass.SassFileContext;
import com.cathive.sass.SassOptions;
import com.cathive.sass.jna.SassLibrary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.management.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;

/**
 * @author Benjamin P. Jung
 */
public class SassCompiler implements SassCompilerMXBean,
                                     MBeanRegistration {

    /** Desired MBean object name. */
    public static final String OBJECT_NAME  = "com.cathive.sass:type=SassCompiler";

    /**
     * Private constructor to avoid instantiation.
     */
    private SassCompiler() {
        super();
    }

    @Override
    public ObjectName preRegister(final MBeanServer server, final ObjectName name) throws Exception {
        return name == null ? new ObjectName(OBJECT_NAME) : name;
    }

    @Override
    public void postRegister(final Boolean registrationDone) {
        // Nothing to do.
    }

    @Override
    public void preDeregister() throws Exception {
        // Nothing to do.
    }

    @Override
    public void postDeregister() {
        // Nothing to do.
    }


    @Override
    public String getLibsassVersion() {
        // Just delegate to the underlying native library.
        return SassLibrary.INSTANCE.libsass_version();
    }

    @Override
    public String compile(final @Nonnull String inputPath, final @Nullable String outputPath, final @Nullable String[] includePath) throws IOException {

        final SassContext context = SassFileContext.create(Paths.get(inputPath));
        final SassOptions options = context.getOptions();

        if (outputPath != null) {
            options.setOutputPath(Paths.get(outputPath));
        }
        if (includePath != null) {
            options.setIncludePath(includePath);
        }

        final ByteArrayOutputStream stringOutput = new ByteArrayOutputStream(1024);
        context.compile(outputPath == null ? stringOutput
                                           : new TeeOutputStream(new FileOutputStream(outputPath), stringOutput));

        return stringOutput.toString("UTF-8");

    }

    /**
     * Registers the SassCompiler MBean with the platform MBean server.
     * @return
     *     The registered object instance.
     * @throws Exception
     *     If anything goes wrong during registration.
     */
    public static ObjectInstance registerMBean() throws Exception {
        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        final ObjectName mbeanName = new ObjectName(OBJECT_NAME);
        return server.registerMBean(new SassCompiler(), mbeanName);
    }

    /**
     * Unregisters the SassCompiler MBean from the platform MBean server.
     * @throws Exception
     *     If anything goes wrong during un-registration.
     */
    public static void unregisterMBean() throws Exception {
        final MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.unregisterMBean(ObjectName.getInstance(OBJECT_NAME));
    }


    /**
     * Helper class that allows us to write to two streams at once.
     */
    private static class TeeOutputStream extends OutputStream {
        private final OutputStream out;
        private final OutputStream tee;
        private TeeOutputStream(final OutputStream out, final OutputStream tee) {
            super();
            this.out = out;
            this.tee = tee;
        }
        @Override
        public void write(final int b) throws IOException {
            out.write(b);
            tee.write(b);
        }
        @Override
        public void write(@Nonnull final byte[] b) throws IOException {
            out.write(b);
            tee.write(b);
        }
        @Override
        public void write(@Nonnull final byte[] b, final int off, final int len) throws IOException {
            out.write(b, off, len);
            tee.write(b, off, len);
        }
        @Override
        public void flush() throws IOException {
            out.flush();
            tee.flush();
        }
        @Override
        public void close() throws IOException {
            out.close();
            tee.close();
        }
    }

}
