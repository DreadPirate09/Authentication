package com.example.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class RegisterUnitTest {


    @Test
    public void test_isMatch_method() {
        String password = "password";
        String Password$ = "Password$";
        assertEquals(false, new MessageDialog("req").isMatches(password));
        assertEquals(true, new MessageDialog("req").isMatches(Password$));
    }

    @Test
    public void test_md5_method() {
        String password = "password";
        String md5hashForPassword = new MD5().getMd5(password);
        assertNotEquals(password,md5hashForPassword);
        assertEquals(md5hashForPassword, new MD5().getMd5("password"));
    }
}
