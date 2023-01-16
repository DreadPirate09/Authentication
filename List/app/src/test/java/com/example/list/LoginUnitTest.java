package com.example.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LoginUnitTest {

    @Test
    public void test_md5_method() {
        String password = "password";
        String md5hashForPassword = new MD5().getMd5(password);
        assertNotEquals(password,md5hashForPassword);
        assertEquals(md5hashForPassword, new MD5().getMd5("password"));
    }
}