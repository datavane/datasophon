package com.datasophon.api.utils;

import com.datasophon.common.Constants;
import com.datasophon.common.cache.CacheUtils;
import com.datasophon.dao.entity.ClusterServiceRoleInstanceEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class StarRocksUtils {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String username="root";
        String password = "";
        String url = "jdbc:mysql://ddp2:9030";
        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "show proc '/frontends'";
        //执行sql，返回结果集
        ResultSet resultSet = statement.executeQuery(sql);
        int columnCount = resultSet.getMetaData().getColumnCount();

        //结果封装
        while (resultSet.next()){
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(resultSet.getString(i));
            }

        }
    }
    private static final Logger logger = LoggerFactory.getLogger(SshTools.class);

    public static void generateStarRocksHa( List<ClusterServiceRoleInstanceEntity> roleInstanceList,Integer clusterId) throws ClassNotFoundException, SQLException {
        Map<String,String> globalVariables = (Map<String, String>) CacheUtils.get("globalVariables"+ Constants.UNDERLINE+clusterId);
        String feMaster = globalVariables.get("${feMaster}");
        logger.info("fe master is {}",feMaster);
        String username="root";
        String password = "";
        String url = "jdbc:mysql://"+feMaster+":9030";
        //加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        for (ClusterServiceRoleInstanceEntity roleInstanceEntity : roleInstanceList) {
            if("FE".equals(roleInstanceEntity.getServiceRoleName())){
                logger.info("generate fe {} to cluster",roleInstanceEntity.getHostname());
                //编写sql
                if(Objects.nonNull(connection) && Objects.nonNull(statement)){
                    String sql = "ALTER SYSTEM add FOLLOWER \""+roleInstanceEntity.getHostname()+":9010\";";
                    //执行sql，返回结果集
                    statement.executeUpdate(sql);
                }
            }else{
                logger.info("generate be {} to cluster",roleInstanceEntity.getHostname());
                //编写sql
                if(Objects.nonNull(connection) && Objects.nonNull(statement)){
                    String sql = "ALTER SYSTEM add BACKEND  \""+roleInstanceEntity.getHostname()+":9050\";";
                    //执行sql，返回结果集
                    statement.executeUpdate(sql);
                }
            }
        }
        //关闭连接  先创建的最后关闭
        if(Objects.nonNull(connection) && Objects.nonNull(statement)){
            statement.close();
            connection.close();
        }
    }

}
