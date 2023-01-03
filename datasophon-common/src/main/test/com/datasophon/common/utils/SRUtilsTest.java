package com.datasophon.common.utils;

import org.junit.Test;

import java.sql.SQLException;

public class SRUtilsTest {
    @Test
    public void testAddBackend() throws SQLException, ClassNotFoundException {
        StarRocksUtils.allBackend("ddp4","ddp5");
    }
}
