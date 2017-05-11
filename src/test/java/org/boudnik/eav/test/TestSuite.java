package org.boudnik.eav.test;


import org.boudnik.eav.Store;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author Alexandre_Boudnik
 * @since 05/12/17 01:09
 */
public class TestSuite {

    private Store store = new Store();

    @Before
    public void setUp() {
        store.clear();
    }

    @Test
    public void testPut() {
        store.put(100, "pet", new HashMap() {{
            put("name", "Swante's dog Bimbo");
            put("cost", 100_000_000000.);
        }}, Store.REPLACE);
        store.put(1, "person", new HashMap() {{
            put("name", "Swante Swensson");
            put("salary", 0);
        }}, Store.REPLACE);
        store.put(1, "property", new HashMap() {{
            put("description", "steam engine");
            put("condition", "broken");
        }}, Store.REPLACE);
        store.put(1, "property", new HashMap() {{
            put("value", 1.);
        }}, Store.ADD);
        store.put(2, "person", new HashMap() {{
            put("name", "Karlsson");
            put("salary", -1.);
        }}, Store.REPLACE);
        store.put(2, "realEstate", new HashMap() {{
            put("type", "penthouse");
            put("address", "on the roof");
            put("value", 100);
        }}, Store.REPLACE);
        store.print();
    }

}
