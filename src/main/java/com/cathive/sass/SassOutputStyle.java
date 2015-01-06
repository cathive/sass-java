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

/**
 * Definition of the Sass output style to be used.
 * <p>See <a href="http://sass-lang.com/documentation/file.SASS_REFERENCE.html#output_style">http://sass-lang.com/documentation/file.SASS_REFERENCE.html#output_style</a>
 * for further details.</p>
 * @author Benjamin P. Jung
 */
public enum SassOutputStyle {

    /**
     * Nested style is the default Sass style, because it reflects the structure of the CSS styles and the HTML
     * document they’re styling. Each property has its own line, but the indentation isn’t constant.
     * Each rule is indented based on how deeply it’s nested.
     */
    NESTED(0),

    /**
     * Expanded is a more typical human-made CSS style, with each property and rule taking up one line.
     * Properties are indented within the rules, but the rules aren’t indented in any special way.
     */
    EXPANDED(1),

    /**
     * Compact style takes up less space than Nested or Expanded.
     * It also draws the focus more to the selectors than to their properties.
     * Each CSS rule takes up only one line, with every property defined on that line.
     * Nested rules are placed next to each other with no newline, while separate groups of rules have
     * newlines between them.
     */
    COMPACT(2),

    /**
     * Compressed style takes up the minimum amount of space possible, having no whitespace except that necessary to
     * separate selectors and a newline at the end of the file. It also includes some other minor compressions, such as
     * choosing the smallest representation for colors. It’s not meant to be human-readable.
     */
    COMPRESSED(3);

    /** Numeric representation of this enum as seen in native code. */
    private final int intValue;

    /**
     * Private constructor
     * @param intValue
     *     Numeric representation of the enum as seen in native code.
     */
    private SassOutputStyle(final int intValue) {
        this.intValue = intValue;
    }

    int getIntValue() {
        return this.intValue;
    }

}