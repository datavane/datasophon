package com.datasophon.common.utils;

import com.datasophon.common.model.ProcInfo;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SRUtilsTest {
    @Test
    public void testAddBackend() throws SQLException, ClassNotFoundException {
        StarRocksUtils.addBackend("ddp4","ddp5");
    }

    @Test
    public void testAddFrontend() throws SQLException, ClassNotFoundException {
        StarRocksUtils.addFollower("ddp4","ddp5");

    }
    @Test
    public void testShowfrontends() throws SQLException, ClassNotFoundException {
        List<ProcInfo> list = StarRocksUtils.showFrontends("ddp4");
        System.out.println(list.toString());
    }

    @Test
    public void testShowBackends() throws SQLException, ClassNotFoundException {
        List<ProcInfo> list = StarRocksUtils.showBackends("ddp4");
        System.out.println(list.toString());
    }
    @Test
    public void testQuery() throws SQLException, ClassNotFoundException {

        StarRocksUtils.executeQuerySql("ddp4","select * from information_schema.`columns`");

    }
}
