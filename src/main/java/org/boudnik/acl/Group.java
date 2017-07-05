package org.boudnik.acl;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey_Kalinov
 * @author Alexandre_Boudnik
 * @version 1.0
 * @since 20-Jun-2017 18:43:30
 */
public class Group {
    private String name;
    public Group parent;
    private final long account;
    private Set<?> members;


    public Group(String name, Group parent, long account) {
        this(name, parent, account, new ArrayList<>());
    }

    public Group(String name, Group parent, long account, @NotNull Collection<?> members) {
        this.name = name;
        this.parent = parent;
        this.account = account;
        this.members = new HashSet<>(members);
    }

    public boolean contains(Object o) {
        return members.contains(o);
    }

    boolean hasAccount(long account) {
        return this.account == account;
    }
}