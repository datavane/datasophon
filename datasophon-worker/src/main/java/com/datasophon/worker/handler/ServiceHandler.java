package com.datasophon.worker.handler;



import com.datasophon.common.Constants;
import com.datasophon.common.model.ServiceRoleRunner;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.PropertyUtils;
import com.datasophon.common.utils.ShellUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


public class ServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServiceHandler.class);

    public ExecResult start(ServiceRoleRunner startRunner,ServiceRoleRunner statusRunner, String decompressPackageName,String runAs) {
        ExecResult statusResult = execRunner(statusRunner, decompressPackageName,null);
        if(statusResult.getExecResult()){
            //已经启动，直接返回
            logger.info("{} already started",decompressPackageName);
            ExecResult execResult = new ExecResult();
            execResult.setExecResult(true);
            return execResult;
        }
        //执行启动脚本
        ExecResult startResult = execRunner(startRunner, decompressPackageName,runAs);
        //检测是否启动成功
        if(startResult.getExecResult()){
            int times = PropertyUtils.getInt("times");;
            int count = 0;
            while (count < times){
                logger.info("check start result at times {}",count+1);
                ExecResult result = execRunner(statusRunner, decompressPackageName,null);
                if(result.getExecResult()){
                    logger.info("start success in {}",decompressPackageName);
                    break;
                }else{
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                count++;
            }
            if(count == times){//超时，置为失败
                logger.info(" start {} timeout",decompressPackageName);
                startResult.setExecResult(false);
            }
        }
        return startResult;
    }


    public ExecResult stop(ServiceRoleRunner runner, ServiceRoleRunner statusRunner,String decompressPackageName) {
        ExecResult statusResult = execRunner(statusRunner, decompressPackageName,null);
        ExecResult execResult = new ExecResult();
        if(statusResult.getExecResult()){
            execResult = execRunner(runner, decompressPackageName,null);
            //检测是否停止成功
            if(execResult.getExecResult()){
                int times = PropertyUtils.getInt("times");
                int count = 0;
                while (count < times){
                    logger.info("check stop result at times {}",count+1);
                    ExecResult result = execRunner(statusRunner, decompressPackageName,null);
                    if(!result.getExecResult()){
                        logger.info("stop success in {}",decompressPackageName);
                        break;
                    }else{
                        try {
                            Thread.sleep(5 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                }
                if(count == times){//超时，置为失败
                    execResult.setExecResult(false);
                }
            }
        }else{//已经是停止状态，直接返回
            logger.info("{} already stopped",decompressPackageName);
            execResult.setExecResult(true);
        }
        return execResult;
    }

    public ExecResult reStart(ServiceRoleRunner runner, String decompressPackageName) {
        ExecResult result = execRunner(runner, decompressPackageName,null);
        return result;
    }

    public ExecResult status(ServiceRoleRunner runner, String decompressPackageName) {
        ExecResult result = execRunner(runner, decompressPackageName,null);
        return result;
    }

    private ExecResult execRunner(ServiceRoleRunner runner, String decompressPackageName,String runAs) {
        String shell = runner.getProgram();
        List<String> args = runner.getArgs();
        long timeout = Long.parseLong(runner.getTimeout());
        ArrayList<String> command = new ArrayList<>();
        if(StringUtils.isNotBlank(runAs)){
            command.add("sudo");
            command.add("-u");
            command.add(runAs);
        }
        if(runner.getProgram().contains("taskmanager") || runner.getProgram().contains("jobmanager")){
            logger.info("do not use sh");
        }else {
            command.add("sh");
        }
        command.add(shell);
        command.addAll(args);
        logger.info("execute shell command : {}",command.toString());
        ExecResult execResult = ShellUtils.execWithStatus(Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName, command, timeout);
        return execResult;
    }

}
