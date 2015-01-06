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

import com.sun.jna.Platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * @author Benjamin P. Jung
 */
public class SassC {

    /** Path to the native "sassc" executable. */
    private Path nativeExecutable;

    /**
     * Default constructor.
     */
    protected SassC() throws IOException {

        // We will store the native executable in a temporary location.
        this.nativeExecutable = Files.createTempFile("sassc", ".exe");

        // Determine the location of the sassc executable on the classpath.
        final StringBuilder sb = new StringBuilder(getNativeLibraryResourcePrefix());
        sb.append("/");
        sb.append("sassc");
        if (Platform.isWindows()) {
            sb.append(".exe");
        }

        // Copies the sassc exectuable to the temporary location.
        Files.copy(this.getClass().getClassLoader().getResourceAsStream(sb.toString()), this.nativeExecutable);

    }

    /**
     * Returns a SassC instance that can be used to compilse SCSS data.
     * @return
     *     A SassC instance that wraps around the native SassC binary.
     */
    public static SassC getInstance() {
        try {
            return new SassC();
        } catch (final IOException e) {
            throw new IllegalStateException("Couldn't initialze SassC wrapper...", e);
        }
    }

    /**
     * NOTE: THIS METHOD IS A 1:1 COPY EXTRACTED FROM THE JNA CODEBASE.
     * @see com.sun.jna.Platform#getNativeLibraryResourcePrefix()
     */
    static String getNativeLibraryResourcePrefix() {
        return getNativeLibraryResourcePrefix(Platform.getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
    }

    /**
     * NOTE: THIS METHOD IS A 1:1 COPY EXTRACTED FROM THE JNA CODEBASE.
     * @see com.sun.jna.Platform#getNativeLibraryResourcePrefix(int, String, String)
     */
    static String getNativeLibraryResourcePrefix(int osType, String arch, String name) {
        String osPrefix;
        arch = arch.toLowerCase().trim();
        if ("powerpc".equals(arch)) {
            arch = "ppc";
        }
        else if ("powerpc64".equals(arch)) {
            arch = "ppc64";
        }
        else if ("i386".equals(arch)) {
            arch = "x86";
        }
        else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
            arch = "x86-64";
        }
        switch(osType) {
            case Platform.ANDROID:
                if (arch.startsWith("arm")) {
                    arch = "arm";
                }
                osPrefix = "android-" + arch;
                break;
            case Platform.WINDOWS:
                osPrefix = "win32-" + arch;
                break;
            case Platform.WINDOWSCE:
                osPrefix = "w32ce-" + arch;
                break;
            case Platform.MAC:
                osPrefix = "darwin";
                break;
            case Platform.LINUX:
                osPrefix = "linux-" + arch;
                break;
            case Platform.SOLARIS:
                osPrefix = "sunos-" + arch;
                break;
            case Platform.FREEBSD:
                osPrefix = "freebsd-" + arch;
                break;
            case Platform.OPENBSD:
                osPrefix = "openbsd-" + arch;
                break;
            case Platform.NETBSD:
                osPrefix = "netbsd-" + arch;
                break;
            case Platform.KFREEBSD:
                osPrefix = "kfreebsd-" + arch;
                break;
            default:
                osPrefix = name.toLowerCase();
                int space = osPrefix.indexOf(" ");
                if (space != -1) {
                    osPrefix = osPrefix.substring(0, space);
                }
                osPrefix += "-" + arch;
                break;
        }
        return osPrefix;
    }

    @Override
    protected void finalize() throws Throwable {
        Files.deleteIfExists(this.nativeExecutable);
        super.finalize();
    }

    public static void main(final String ... args) {
        // TODO Read args and wrap around sassc executable.
    }

}
