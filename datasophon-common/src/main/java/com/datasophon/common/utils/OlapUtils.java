/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.datasophon.common.utils;

import com.datasophon.common.model.ProcInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OlapUtils {

    private static final Logger logger = LoggerFactory.getLogger(OlapUtils.class);

    public static void addFollower(String feMaster, String hostname) throws SQLException, ClassNotFoundException {
        String sql = "ALTER SYSTEM add FOLLOWER \"" + hostname + ":9010\";";
        logger.info("sql is {}", sql);
        executeSql(feMaster, hostname, sql);
    }

    public static void addBackend(String feMaster, String hostname) throws SQLException, ClassNotFoundException {
        String sql = "ALTER SYSTEM add BACKEND  \"" + hostname + ":9050\";";
        logger.info("sql is {}", sql);
        executeSql(feMaster, hostname, sql);
    }

    private static void executeSql(String feMaster, String hostname,
                                   String sql) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection(feMaster);
        Statement statement = connection.createStatement();
        logger.info("generate be {} to cluster", hostname);
        if (Objects.nonNull(connection) && Objects.nonNull(statement)) {
            statement.executeUpdate(sql);
        }
        close(connection, statement);
    }

    public static ExecResult addFollowerBySqlClient(String feMaster,
                                                    String hostname) {
        String sqlCommand =
                "mysql -h"
                        + feMaster
                        + " -uroot -P9030 -e"
                        + " 'ALTER SYSTEM add FOLLOWER  \""
                        + hostname
                        + ":9010\"';";
        // logger.info("sqlCommand is {}", sqlCommand);
        return ShellUtils.exceShell(sqlCommand);
    }

    public static ExecResult addBackendBySqlClient(String feMaster,
                                                   String hostname) {
        String sqlCommand =
                "mysql -h"
                        + feMaster
                        + " -uroot -P9030 -e"
                        + " 'ALTER SYSTEM add BACKEND  \""
                        + hostname
                        + ":9050\"';";
        // logger.info("sqlCommand is {}", sqlCommand);
        return ShellUtils.exceShell(sqlCommand);
    }

    private static Connection getConnection(String feMaster) throws ClassNotFoundException, SQLException {
        String username = "root";
        String password = "";
        String url = "jdbc:mysql://" + feMaster + ":9030";
        // 加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    private static void close(Connection connection, Statement statement) throws SQLException {
        if (Objects.nonNull(connection) && Objects.nonNull(statement)) {
            statement.close();
            connection.close();
        }
    }

    public static List<ProcInfo> showFrontends(String feMaster) throws SQLException, ClassNotFoundException {
        String sql = "SHOW PROC '/frontends';";
        // logger.info("sql is {}", sql);
        return executeQueryProcInfo(feMaster, sql);
    }

    public static List<ProcInfo> listDeadFrontends(String feMaster) throws SQLException, ClassNotFoundException {
        String sql = "SHOW PROC '/frontends';";
        // logger.info("sql is {}", sql);
        return getDeadProcInfos(feMaster, sql);
    }

    public static List<ProcInfo> listDeadBackends(String feMaster) throws SQLException, ClassNotFoundException {
        String sql = "SHOW PROC '/frontends';";
        // logger.info("sql is {}",sql);
        return getDeadProcInfos(feMaster, sql);
    }

    public static List<ProcInfo> showBackends(String feMaster) throws SQLException, ClassNotFoundException {
        String sql = "SHOW PROC '/backends';";
        // logger.info("sql is {}",sql);
        return executeQueryProcInfo(feMaster, sql);
    }

    public static List<ProcInfo> executeQueryProcInfo(String feMaster,
                                                      String sql) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection(feMaster);
        Statement statement = connection.createStatement();
        ArrayList<ProcInfo> list = new ArrayList<>();
        if (Objects.nonNull(connection) && Objects.nonNull(statement)) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ProcInfo procInfo = new ProcInfo();
                procInfo.setHostName(resultSet.getString("HostName"));
                procInfo.setAlive(resultSet.getBoolean("Alive"));
                procInfo.setErrMsg(resultSet.getString("ErrMsg"));
                list.add(procInfo);
            }
        }
        close(connection, statement);
        return list;
    }

    public static List<ProcInfo> executeQuerySql(String feMaster,
                                                 String sql) throws SQLException, ClassNotFoundException {
        Connection connection = getConnection(feMaster);
        long start = System.currentTimeMillis();
        Statement statement = connection.createStatement();
        ArrayList<ProcInfo> list = new ArrayList<>();
        if (Objects.nonNull(connection) && Objects.nonNull(statement)) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        close(connection, statement);
        return list;
    }

    private static List<ProcInfo> getDeadProcInfos(String feMaster,
                                                   String sql) throws SQLException, ClassNotFoundException {
        List<ProcInfo> list = executeQueryProcInfo(feMaster, sql);
        ArrayList<ProcInfo> deadList = new ArrayList<>();
        for (ProcInfo procInfo : list) {
            if (!procInfo.getAlive()) {
                deadList.add(procInfo);
            }
        }
        return deadList;
    }
}
