package org.boudnik.acl;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.jcp.xml.dsig.internal.dom.XMLDSigRI;

/**
 * @author Sergey_Kalinov
 * @author Alexandre_Boudnik
 * @version 1.0
 * @since 20-Jun-2017 18:43:31
 */
public class Person {

    private char firstName;
    private char lastName;
    private String DN; // id from certificate
    public Actor[] actors;
    @QuerySqlField String aa;

}