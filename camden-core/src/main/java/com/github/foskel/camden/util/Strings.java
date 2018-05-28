package com.github.foskel.camden.util;

public final class Strings {
    private Strings() {
    }

    public static boolean startsWithIgnoreCase(String target, String prefix) {
        if (target != null && prefix != null) {
            return prefix.length() <= target.length() && regionMatches(target, true, 0, prefix, 0, prefix.length());
        } else {
            return target == null && prefix == null;
        }
    }

    private static boolean regionMatches(CharSequence target,
                                         boolean ignoreCase,
                                         int thisStart,
                                         CharSequence substring,
                                         int start,
                                         int length) {
        if (target instanceof String && substring instanceof String) {
            return ((String) target).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int tmpLen = length;
            int srcLen = target.length() - thisStart;
            int otherLen = substring.length() - start;
            if (thisStart >= 0 && start >= 0 && length >= 0) {
                if (srcLen >= length && otherLen >= length) {
                    while (tmpLen-- > 0) {
                        char c1 = target.charAt(index1++);
                        char c2 = substring.charAt(index2++);
                        if (c1 != c2) {
                            if (!ignoreCase) {
                                return false;
                            }

                            if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                                return false;
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
