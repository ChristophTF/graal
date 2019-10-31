/*
 * Copyright (c) 2018, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oracle.truffle.regex.tregex.matchers;

import static com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import static com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;

import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.regex.charset.CharSet;

/**
 * Character range matcher using a sorted list of ranges.
 */
public abstract class RangeListMatcher extends InvertibleCharMatcher {

    @CompilationFinal(dimensions = 1) private final char[] ranges;

    /**
     * Constructs a new {@link RangeListMatcher}.
     * 
     * @param invert see {@link InvertibleCharMatcher}.
     * @param ranges a sorted array of character ranges in the form [lower inclusive bound of range
     *            0, higher inclusive bound of range 0, lower inclusive bound of range 1, higher
     *            inclusive bound of range 1, ...]. The array contents are not modified by this
     *            method.
     */
    RangeListMatcher(boolean invert, char[] ranges) {
        super(invert);
        this.ranges = ranges;
    }

    public static RangeListMatcher create(boolean invert, char[] ranges) {
        return RangeListMatcherNodeGen.create(invert, ranges);
    }

    @Specialization
    @ExplodeLoop(kind = ExplodeLoop.LoopExplosionKind.FULL_UNROLL)
    public boolean match(char c, boolean compactString) {
        for (int i = 0; i < ranges.length; i += 2) {
            final char lo = ranges[i];
            final char hi = ranges[i + 1];
            if (compactString && lo > 255) {
                return result(false);
            }
            if (isSingleChar(lo, hi)) {
                // do simple equality checks on ranges that contain a single character
                if (lo == c) {
                    return result(true);
                }
            } else if (isTwoChars(lo, hi)) {
                // do simple equality checks on ranges that contain two characters
                if (c == lo || c == hi) {
                    return result(true);
                }
            } else {
                if (lo <= c) {
                    if (hi >= c) {
                        return result(true);
                    }
                } else {
                    return result(false);
                }
            }
        }
        return result(false);
    }

    private static boolean isSingleChar(char lo, char hi) {
        CompilerAsserts.partialEvaluationConstant(lo);
        CompilerAsserts.partialEvaluationConstant(hi);
        return lo == hi;
    }

    private static boolean isTwoChars(char lo, char hi) {
        CompilerAsserts.partialEvaluationConstant(lo);
        CompilerAsserts.partialEvaluationConstant(hi);
        return lo + 1 == hi;
    }

    @Override
    public int estimatedCost() {
        return ranges.length;
    }

    @Override
    @TruffleBoundary
    public String toString() {
        return "list " + modifiersToString() + "[" + CharSet.rangesToString(ranges) + "]";
    }
}
