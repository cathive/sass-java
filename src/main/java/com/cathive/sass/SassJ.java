package com.cathive.sass;

import java.nio.file.Paths;

/**
 * @author Benjamin P. Jung
 */
public class SassJ {

    /**
     * Main loop.
     * @param args
     *   Command line arguments.
     * @throws Exception
     *   If compilation fails.
     */
    public static void main(final String... args) throws Exception {
        final SassContext context = SassFileContext.create(Paths.get(".").toAbsolutePath().resolve(Paths.get(args[0])));
        context.compile(System.out);
    }

}
