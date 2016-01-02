libsass for Java
================

A [JNA](https://github.com/twall/jna) binding to access [libsass](http://libsass.org/) functionality.

A compiled and ready-to-use version of this library can be found in the in the [Maven Central repository](http://search.maven.org/#browse%7C1800775426).
To use the library in your Maven based projects just add the following lines to your
'pom.xml':

```xml
<dependency>
  <groupId>com.cathive.sass</groupId>
  <artifactId>sass-java</artifactId>
  <version>${sass-java.version}</version>
</dependency>
```

## Versions

libsass for Java uses [Semantic Versioning](http://www.semver.org/).
MAJOR, MINOR and PATCH version are used for the library / Java binding itself.
The BUILD METADATA component of the version is used to describe to version of the
underlying native C/C++ libsass component.

## Native libraries

Compiled dynamic libraries of libsass are bundled inside of the JAR artifact together with the required auto-generated JNA binding classes and nice wrapper classes to allow for a Java-like feeling when working with libsass.

### Supported platforms

This is the list of platforms that are directly supported, because the dynamic library has been pre-compiled and bundled:

- [ ] Linux x86-64
- [ ] Linux x86
- [ ] Mac OS X x86-64
- [x] Windows x86-64
- [x] Windows x86

If your desired platform / architecture is missing, feel free to open an issue and add a pre-compiled version of libsass for inclusion!

## Example code

```java
import com.cathive.sass.SassCompilationException;
import com.cathive.sass.SassContext;
import com.cathive.sass.SassFileContext;
import com.cathive.sass.SassOptions;
import com.cathive.sass.SassOutputStyle;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A little example to demonstrate some of the features of sass-java.
 */
class SimpleSassExample {

    public static void main(String... args) {

        // Our root directory that contains the
        Path srcRoot = Paths.get("/path/to/my/scss/files");

        // Creates a new sass file context.
        SassContext ctx = SassFileContext.create(srcRoot.resolve("styles.scss"));

        SassOptions options = ctx.getOptions();
        options.setIncludePath(
                srcRoot,
                Paths.get("/another/include/directory"),
                Paths.get("/and/yet/another/include/directory")
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
            System.err.println(String.format("Compilation failed: %s", e.getMessage()));
        }
    }

}
```
## Ant Task Example

This example shows how to invoke sass-java from Ant using the bundled Ant task and the maven-antrun-plugin.

```xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>1.7</version>
    <executions>
        <execution>
            <phase>generate-sources</phase>
            <configuration>
                <target>
                    <path id="plugin.classpath">
                        <path path="${maven.plugin.classpath}"/>
                    </path>
                    <taskdef name="sass" classname="com.cathive.sass.SassTask" classpathref="plugin.classpath"/>
                    <delete dir="${output.dir}"/>
                    <sass in="${sass.srcdir}" outdir="${output.dir}">
                        <!--
                            Note that the task takes a nested `path` element to reference any Sass include directories.
                        -->
                        <path>
                            <pathelement location="${include1.dir}"/>
                            <pathelement location="${include2.dir}"/>
                        </path>
                    </sass>
                </target>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
    <dependencies>
        <dependency>
            <groupId>com.cathive.sass</groupId>
            <artifactId>sass-java</artifactId>
            <version>${sass-java.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.ant</groupId>
            <artifactId>ant</artifactId>
            <version>1.9.6</version>
        </dependency>
    </dependencies>
</plugin>
```

### Ant Task Attributes
`in` (Path to a directory that contains scss files or a single scss file)

`outdir` (Directory path where the compiled css should be placed)

`precision` (number)

`outputstyle` (0 = nested, 1 = expanded, 2 = compact, 3 = compressed)

`sourcecomments` (true/false)

`sourcemapembed` (true/false)

`sourcemapcontents` (true/false)

`omitsourcemapurl` (true/false)

`isindentedsyntaxsrc` (true/false)

`sourcemapfile` (Path to source map file)

`sourcemaproot` (Directly inserted in source maps)
