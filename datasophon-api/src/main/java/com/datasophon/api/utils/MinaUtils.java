package com.datasophon.api.utils;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MinaUtils {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(MinaUtils.class);

    private String sshHost;
    private Integer sshPort;
    private String sshUser;
    private String privateKey;

    private SshClient sshClient;
    private ClientSession session;
    private SftpFileSystem sftp;

    public MinaUtils(String sshHost, Integer sshPort, String sshUser, String privateKey) {
        this.sshHost = sshHost;
        this.sshPort = sshPort;
        this.sshUser = sshUser;
        this.privateKey = privateKey;
    }


    /**
     * 打开远程会话
     */
    public ClientSession openConnection() {

        this.sshClient = SshClient.setUpDefaultClient();
        this.sshClient.start();
        try {
            this.session = this.sshClient.connect(sshUser, sshHost, sshPort).verify().getClientSession();
            this.session.addPublicKeyIdentity(getKeyPairFromString(privateKey));
            if (this.session.auth().verify().isFailure()) {
                LOG.info("验证失败");
                return null;
            }
            this.sftp = SftpClientFactory.instance().createSftpFileSystem(this.session);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info(sshHost + " 连接成功");
        return this.session;
    }

    /**
     * 关闭远程会话
     */
    public void closeConnection() {
        try {
            this.session.close();
            this.sftp.close();
            this.sshClient.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密钥对
     */
    static KeyPair getKeyPairFromString(String pk) {
        final KeyPairGenerator rsa;
        try {
            rsa = KeyPairGenerator.getInstance("RSA");
            final KeyPair keyPair = rsa.generateKeyPair();
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(pk.getBytes());
            final ObjectOutputStream o = new ObjectOutputStream(stream);
            o.writeObject(keyPair);
            return keyPair;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 同步执行,需要获取执行完的结果
     *
     * @param command 命令
     * @return 结果
     */
    public String execCmdWithResult(String command) {
        this.openConnection();
        LOG.info("exe cmd: {}", command);
        // 命令返回的结果
        ChannelExec ce = null;
        // 返回结果流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 错误信息
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            ce = this.session.createExecChannel(command);
            ce.setOut(out);
            ce.setErr(err);
            // 执行并等待
            ce.open();
            Set<ClientChannelEvent> events =
                    ce.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(10000));
            //  检查请求是否超时
            if (events.contains(ClientChannelEvent.TIMEOUT)) {
                throw new Exception("mina 连接超时");
            }
            this.session.close(false);
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
        return out.toString().trim();
    }

    /**
     * 上传文件,相同路径ui覆盖
     *
     * @param remotePath 远程目录地址
     * @param inputFile  文件 File
     */
    public boolean uploadFile(String remotePath, String inputFile) {
        this.openConnection();
        File uploadFile = new File(inputFile);
        InputStream input = null;
        try {
            Path path = this.sftp.getDefaultDir().resolve(remotePath);
            if (!Files.exists(path)) {
                LOG.info("create pathHome {} ", path);
                Files.createDirectories(path);
            }
            input = Files.newInputStream(uploadFile.toPath());
            Path file = path.resolve(uploadFile.getName());
            if (Files.exists(file)){
                Files.deleteIfExists(file);
            }
            Files.copy(input, file);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                this.sftp.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public boolean createDir(String path) {
        this.openConnection();
        try {
            Path remoteRoot = this.sftp.getDefaultDir().resolve(path);
            if (!Files.exists(remoteRoot)) {
                Files.createDirectories(remoteRoot);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        MinaUtils minaUtils = new MinaUtils("localhost", 22, "liuxin",
                "/Users/liuxin/.ssh/id_rsa");
            String ls = minaUtils.execCmdWithResult("arch");
            System.out.println(ls);
//        minaUtils.closeConnection();
//        boolean dir = minaUtils.createDir("/home/shinow/test/");
//        System.out.println(dir);
//        boolean uploadFile = minaUtils.uploadFile("/Users/liuxin/opt/test", "/Users/liuxin/Downloads/yarn-default.xml");
//        System.out.println(uploadFile);
    }

}
