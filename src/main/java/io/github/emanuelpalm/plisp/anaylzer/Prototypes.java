package io.github.emanuelpalm.plisp.anaylzer;

import java.util.TreeMap;

/**
 * Various {@link Prototype} related utilities.
 */
public class Prototypes {
    /** Default lambda function prototype. */
    public static final Prototype LAMBDA = new Prototype("lambda", 2);

    /** Default label function prototype. */
    public static final Prototype LABEL = new Prototype("label", 2);

    /** Default function prototypes. */
    public static TreeMap<String, Prototype> defaults() {
        final TreeMap<String, Prototype> map = new TreeMap<>();
        map.put("atom", new Prototype("atom", 1));
        map.put("eq", new Prototype("eq", 2));
        map.put("car", new Prototype("car", 1));
        map.put("cdr", new Prototype("cdr", 1));
        map.put("cons", new Prototype("cons", 2));
        map.put("cond", new Prototype("cond", null));
        map.put("lambda", LAMBDA);
        map.put("label", LABEL);
        return map;
    }
}
