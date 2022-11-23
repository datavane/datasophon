package com.datasophon.api.utils;

import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.subsystem.SubsystemClient;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MinaUtils {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MinaUtils.class);


    /**
     * 打开远程会话
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名，如果为null，默认root
     * @param sshPass 密码
     * @return {@link Session}
     */
    public static ClientSession openConnection(String sshHost, Integer sshPort, String sshUser, String sshPass) {

        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = null;
        try {
            session = client.connect(sshUser, sshHost, sshPort).verify().getClientSession();

            session.addPasswordIdentity(sshPass);
            if (session.auth().verify(3000).isFailure()) {
                LOG.info("验证失败");
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info(sshHost + " 连接成功");
        return session;
    }

    /**
     * 开启exec通道
     *
     * @param session Session
     * @return ChanelExec
     */
    public static ChannelExec openChannelExec(ClientSession session) {
        ChannelExec channelExec = null;
        try {
            channelExec = session.createExecChannel("exec");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return channelExec;
    }

    /**
     * 同步执行,需要获取执行完的结果
     *
     * @param session Session
     * @param command 命令
     * @return 结果
     */
    public static String execCmdWithResult(ClientSession session, String command, long timeout) {
        LOG.info("exe cmd: {}", command);
        // 命令返回的结果
        ChannelExec ce = null;
        // 返回结果流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 错误信息
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            ce = session.createExecChannel(command);
            ce.setOut(out);
            ce.setErr(err);
            // Execute and wait
            ce.open();
            Set<ClientChannelEvent> events =
                    ce.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(timeout));
            //  Check if timed out
            if (events.contains(ClientChannelEvent.TIMEOUT)) {
                throw new Exception("mina 连接超时");
            }
            session.close(false);

            int exitStatus = ce.getExitStatus();
            LOG.info("mina result {}", exitStatus);
            if (exitStatus == 1) {
                return "failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ce.isClosed()) {
                try {
                    ce.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        LOG.info("exe cmd return : {}", out);
        return out.toString();
    }


    /**
     * 开启SFTP通道
     *
     * @param session Session
     * @return ChannelSftp
     * @throws Exception
     */
    public static SftpFileSystem openChannelSftp(ClientSession session) {
        SftpFileSystem sftpFileSystem = null;
        try {
            sftpFileSystem = SftpClientFactory.instance().createSftpFileSystem(session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sftpFileSystem;
    }

    /**
     * 上传文件,相同路径ui覆盖
     *
     * @param sftp       sftp
     * @param remotePath 远程目录地址
     * @param inputFile  文件 File
     */
    public static boolean uploadFile(SftpFileSystem sftp, String remotePath, String inputFile) {
        File uploadFile = new File(inputFile);
        FileInputStream input = null;
        Path path = sftp.getDefaultDir().resolve(remotePath);
        try {
            input = new FileInputStream(uploadFile);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path file = path.resolve(uploadFile.getName());
            Files.deleteIfExists(file);
            Files.copy(input, file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建目录
     *
     * @param path
     * @param sftp
     * @return
     */
    public static boolean createDir(String path, SftpFileSystem sftp) {
        try {
            Path remoteRoot = sftp.getDefaultDir().resolve(path);
            if (!Files.exists(remoteRoot)) {
                Files.createDirectories(remoteRoot);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public static void main(String[] args) throws IOException {
        ClientSession session = MinaUtils.openConnection("localhost", 22, "liuxin", "960319");
        SftpFileSystem sftpFileSystem = openChannelSftp(session);
//        String ls = execCmdWithResult(session, "java -version 2>&1 | sed '1!d' | sed -e 's/\"//g' | awk '{print $3}'", 1000);
//        System.out.println(ls);
//        session.close();

//        boolean dir = MinaUtils.createDir("/Users/liuxin/opt/test", sftpFileSystem);
//        System.out.println(dir);
        boolean uploadFile = uploadFile(sftpFileSystem, "/Users/liuxin/opt/test", "/Users/liuxin/opt/datax.tar.gz");
        System.out.println(uploadFile);
    }

}
