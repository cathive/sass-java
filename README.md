libsass for Java
================

A [JNA](https://github.com/twall/jna) binding to access [libsass](http://libsass.org/) functionality.

A compiled and ready-to-use version of this library can be found in the in the [Maven Central repository](http://search.maven.org/#browse%7C1800775426).
To use the library in your Maven based projects just add the following lines to your
'pom.xml':

```xml
<dependency>
  <groupId>com.cathive.sass</groupId>
  <artifactId>com.cathive.sass</artifactId>
  <version>${sass-java.version}</version>
</dependency>
```

## Native libraries

Compiled dynamic libraries of libsass are bundled inside of the JAR artifact together with the required auto-generated JNA binding classes and nice wrapper classes to allow for a Java-like feeling when working with libsass. Currently only a few native platforms are supported. If your desired platform is missing, feel free to open an issue and add a pre-compiled version of libsass for inclusion!

## Example code

```java
import java.nio.Paths;

import com.cathive.sass.SassCompilationException;
import com.cathive.sass.SassContext;
import com.cathive.sass.SassFileContext;
import com.cathive.sass.SassOptions;
import com.cathive.sass.SassOutputStyle;

/**
 * A little example to demonstrate some of the features of sass-java.
 */
class SassExample {

    public static void main(String... args) {
    
        // Our root directory that contains the 
        Path srcRoot = Paths.create("/path/to/my/scss/files");
    
        // Creates a new sass file context.
        SassContext ctx = SassFileContext.create(srcRoot.resolve("styles.scss"));
    
        SassOptions options = ctx.getOptions();
        options.setIncludePath(
                srcRoot,
                Paths.get("/another/include/directory"),
                Paths.get("/and/yet/another/include/directoty"
                //[...] varargs can be passed to add even more include directories.
        );
        options.setOutputStyle(SassOutputStyle.NESTED);
        // any other options supported by libsass including source map stuff can be configured
        // as well here.
        
        // Will print the compiled CSS contents to the console. Use a FileOutputStream
        // or some other fancy mechanism to redirect the output to wherever you want.
        try {
            ctx.compile(System.out);
        } catch (SassCompilationException e) {
            // Will print the error code, filename, line, column and the message provided
            // by libsass to the standard error output.
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(String.format("Compilation failed: %s"), e.getMessage());
        }
    }

}
```

