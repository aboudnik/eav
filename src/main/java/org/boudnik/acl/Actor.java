package org.boudnik.acl;

import java.util.function.Function;

/**
 * @author Sergey_Kalinov
 * @author Alexandre_Boudnik
 * @version 1.0
 * @since 20-Jun-2017 18:43:29
 */
public class Actor {
    private int id;
    private Function[] methods;
    public Group group;
    public Permission permission;
    public RuledMethod ruledMethod;

}