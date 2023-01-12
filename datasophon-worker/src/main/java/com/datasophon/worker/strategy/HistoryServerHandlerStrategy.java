package com.datasophon.worker.strategy;

import com.datasophon.common.command.ServiceRoleOperateCommand;
import com.datasophon.common.enums.CommandType;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.ShellUtils;
import com.datasophon.worker.handler.ServiceHandler;

import java.sql.SQLException;

public class HistoryServerHandlerStrategy implements ServiceRoleStrategy {
    @Override
    public ExecResult handler(ServiceRoleOperateCommand command) throws SQLException, ClassNotFoundException {
        ServiceHandler serviceHandler = new ServiceHandler();
        if (command.getCommandType().equals(CommandType.INSTALL_SERVICE)) {
            //create tmp
            ShellUtils.exceShell("sudo -u hdfs /opt/datasophon/hadoop-3.3.3/bin/hdfs dfs -mkdir -p /user/yarn/yarn-logs");
            ShellUtils.exceShell("sudo -u hdfs /opt/datasophon/hadoop-3.3.3/bin/hdfs dfs -mkdir /tmp");
            ShellUtils.exceShell("sudo -u hdfs /opt/datasophon/hadoop-3.3.3/bin/hdfs dfs -chmod 777 /tmp");
            ShellUtils.exceShell("sudo -u hdfs /opt/datasophon/hadoop-3.3.3/bin/hdfs dfs -chown yarn:hadoop /user/yarn/yarn-logs");
        }
        return serviceHandler.start(command.getStartRunner(), command.getStatusRunner(), command.getDecompressPackageName(), command.getRunAs());
    }
}
