package com.datasophon.api.utils;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.LoggerFactory;

/**
 * @author gaodayu
 * @description: 连接linux工具类, 可实现执行命令和文件上传
 * @create 2021-02-01 18:24
 */
public class JSchUtils {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(JSchUtils.class);

    private static final int TIMEOUT = 5 * 60 * 1000;

    /**
     * 获取session
     *
     * @param host     ip
     * @param port     端口
     * @param username 用户名
     * @param privateKey 密码
     * @return Session
     */
    public static Session getSession(String host, int port, String username, String privateKey) {
        Properties properties = new Properties();
        properties.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        Session session = null;
        try {
            jsch.addIdentity(privateKey);
            session = jsch.getSession(username, host, port);
            session.setConfig(properties);
            // 连接超时
            session.connect(10000);
        } catch (JSchException e) {
            e.printStackTrace();
            return null;
        }
        return session;
    }

    /**
     * 开启exec通道
     *
     * @param session Session
     * @return ChanelExec
     */
    public static ChannelExec openChannelExec(Session session) {
        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return channelExec;
    }

    /**
     * 关闭channelExec
     *
     * @param channelExec ChannelExec
     */
    public static void closeChannelExec(ChannelExec channelExec) {
        if (channelExec != null) {
            channelExec.disconnect();
        }
    }

    /**
     * 异步执行,不需要结果
     *
     * @param session Session
     * @param cmd 命令
     */
    public static void execCmdWithOutResult(Session session, String cmd) {
        ChannelExec channelExec = openChannelExec(session);
        channelExec.setCommand(cmd);
        try {
            channelExec.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        closeChannelExec(channelExec);
    }

    /**
     * 同步执行,需要获取执行完的结果
     *
     * @param session Session
     * @param command 命令
     * @return 结果
     */
    public static String execCmdWithResult(Session session, String command) {
        LOG.info("exe cmd: {}", command);

        byte[] tmp = new byte[1024];
        // 命令返回的结果
        StringBuilder resultBuffer = new StringBuilder();
        ChannelExec exec = null;

        try {
            exec = (ChannelExec) session.openChannel("exec");
            exec.setCommand(command);
            exec.connect();
            // 返回结果流（命令执行错误的信息通过getErrStream获取）
            InputStream stdStream = exec.getInputStream();
            // 开始获得SSH命令的结果
            while (true) {
                while (stdStream.available() > 0) {
                    int i = stdStream.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    resultBuffer.append(new String(tmp, 0, i));
                }
                if (exec.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    LOG.error("执行命令发生错误!", e);
                }
            }
            int exitStatus = exec.getExitStatus();
            LOG.info("jsch result {}",exitStatus);
            if(exitStatus == 1){
                return "failed";
            }
        }catch (Exception e){
          e.printStackTrace();
          return null;
        } finally {
            if (exec.isConnected()) {
                exec.disconnect();
            }
        }
        String result = resultBuffer.toString().trim();
        LOG.info("exe cmd return : {}", result);
        return result;
    }

    /**
     * 开启SFTP通道
     *
     * @param session Session
     * @return ChannelSftp
     * @throws Exception
     */
    public static ChannelSftp openChannelSftp(Session session) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        return channelSftp;
    }

    /**
     * 关闭ChannelSftp
     *
     * @param channelSftp ChannelSftp
     */
    public static void closeChannelSftp(ChannelSftp channelSftp) {
        if (channelSftp != null) {
            channelSftp.disconnect();
        }
    }

    /**
     * 上传文件,相同路径ui覆盖
     *
     * @param session Session
     * @param remotePath 远程目录地址
     * @param inputFile 文件 File
     */
    public static boolean uploadFile(Session session, String remotePath, String inputFile) {
        File uploadFile = new File(inputFile);
        ChannelSftp channelSftp = null;
        FileInputStream input = null;

        try {
            channelSftp = openChannelSftp(session);
            input = new FileInputStream(uploadFile);
            if (createDir(remotePath, channelSftp)) {
                LOG.info("found pathHome {} ", remotePath);
                channelSftp.cd(remotePath);
            }
            channelSftp.put(input, uploadFile.getName());
            channelSftp.disconnect();
            LOG.info("File upload successfully: {}", uploadFile.getPath());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 创建目录
     *
     * @param createpath
     * @return
     */
    public static boolean createDir(String createpath,ChannelSftp sftp) {
        try {
            if (isDirExist(createpath,sftp)) {
                sftp.cd(createpath);
                return true;
            }
            String pathArry[] = createpath.split("/");
            StringBuffer filePath = new StringBuffer("/");
            for (String path : pathArry) {
                if (path.equals("")) {
                    continue;
                }
                filePath.append(path + "/");
                if (isDirExist(filePath.toString(),sftp)) {
                    sftp.cd(filePath.toString());
                } else {
                    // 建立目录
                    sftp.mkdir(filePath.toString());
                    // 进入并设置为当前目录
                    sftp.cd(filePath.toString());
                }

            }
            sftp.cd(createpath);
            return true;
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 判断目录是否存在
     *
     * @param directory
     * @return
     */
    public static boolean isDirExist(String directory,ChannelSftp sftp) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(directory);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }


    /**
     * 判断有无路径
     *
     * @param path 路径
     * @return true or false
     */
    public static boolean hasPath(String path, ChannelSftp sftp) {
        try {
            sftp.lstat(path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据进程名获取进程的pid
     *
     * @param session Session
     * @param processName 进程名
     * @return List<String> 进程集合
     */
    public static List<String> getPidLinuxCmd(Session session, String processName) {
        String cmd = "ps -ef|grep " + processName + " | grep -v grep";
        String result = execCmdWithResult(session, cmd);
        String[] arr = result.split("\n");
        List<String> processIds = new ArrayList<>();
        for (int i = 0; i <= arr.length - 1; ++i) {
            if (arr[i].split("\\s+").length < 2) {
                return null;
            }
            String thatPid = arr[i].split("\\s+")[1];
            if ("-f".equals(thatPid)) {
                break;
            }
            processIds.add(thatPid);
        }
        return processIds;
    }

    public static void main(String[] args) {
        Session session = getSession("ddp1015", 22, "root", "D:\\360Downloads\\id_rsa");
        ChannelExec channelExec = openChannelExec(session);
        String s = execCmdWithResult(session, "java -version 2>&1 | sed '1!d' | sed -e 's/\"//g' | awk '{print $3}'");
        System.out.println(s);
        session.disconnect();
    }
}

