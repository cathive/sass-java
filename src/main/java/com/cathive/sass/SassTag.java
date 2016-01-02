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

import com.cathive.sass.jna.SassLibrary;

/**
 * @author Benjamin P. Jung
 */
public enum SassTag {
    SASS_BOOLEAN(SassLibrary.Sass_Tag.SASS_BOOLEAN),
    SASS_NUMBER(SassLibrary.Sass_Tag.SASS_NUMBER),
    SASS_COLOR(SassLibrary.Sass_Tag.SASS_COLOR),
    SASS_STRING(SassLibrary.Sass_Tag.SASS_STRING),
    SASS_LIST(SassLibrary.Sass_Tag.SASS_LIST),
    SASS_MAP(SassLibrary.Sass_Tag.SASS_MAP),
    SASS_NULL(SassLibrary.Sass_Tag.SASS_NULL),
    SASS_ERROR(SassLibrary.Sass_Tag.SASS_ERROR),
    SASS_WARNING(SassLibrary.Sass_Tag.SASS_WARNING);

    /** Sass tag primitive int value. */
    private final int intValue;

    SassTag(final int intValue) {
        this.intValue = intValue;
    }

    int getIntValue() {
        return this.intValue;
    }

}
