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
 * @author Benjamin P. Jung
 */
public enum SassTag {
    SASS_BOOLEAN(0),
    SASS_NUMBER(1),
    SASS_COLOR(2),
    SASS_STRING(3),
    SASS_LIST(4),
    SASS_MAP(5),
    SASS_NULL(6),
    SASS_ERROR(7),
    SASS_WARNING(8);

    /** Sass tag primitive int value. */
    private final int intValue;

    SassTag(final int intValue) {
        this.intValue = intValue;
    }

    int getIntValue() {
        return this.intValue;
    }

}
