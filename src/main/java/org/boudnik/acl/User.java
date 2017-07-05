package org.boudnik.acl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexandre_Boudnik
 * @since 06/30/17 18:50
 */
public class User {
    private final String name;
    private final Set<Actor> actors = new HashSet<>();

    public User(String name) {
        this.name = name;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", actors=" + actors +
                '}';
    }
}
