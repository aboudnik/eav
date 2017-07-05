package org.boudnik.acl;

import java.rmi.AccessException;

import org.junit.Test;

/**
 * @author Alexandre_Boudnik
 * @since 06/30/17 18:42
 */
public class AccessTest {
    @Test
    public void main() throws AccessException {
        Access access = new Access();
        User user = new User("shr");

        access.grant(user, Product.VRKO.ACCOUNTANT, o -> true);
        access.grant(user, Product.VRKO.CONTROLLER, o -> (int) o == 30);

        access.revoke(user, Product.VRKO.ACCOUNTANT, o -> true);

        String toString = access.invoke(user, Product.VRKO.toString, 35);
        System.out.println("toString = " + toString);
    }
}
