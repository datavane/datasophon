package com.datasophon.api.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class SSH2Utils {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SSH2Utils.class);

    /**
     * 连接到服务器
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @return {@link Connection}
     */
    public static Connection connect(String sshHost, int sshPort) {
        Connection connect = new Connection(sshHost, sshPort);
        try {
            connect.connect();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return connect;
    }

    /**
     * 打开远程会话
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名，如果为null，默认root
     * @param sshPass 密码
     * @return {@link Session}
     */
    public static Connection openConnection(String sshHost, Integer sshPort, String sshUser, String sshPass) {
        try {

            Connection connection = new Connection(sshHost, sshPort);
            //建立ssh2连接
            connection.connect();
            //检验用户名
            boolean login = connection.authenticateWithPublicKey(sshUser,new File("/Users/liuxin/.ssh/id_rsa"),null);
//            boolean login = connection.authenticateWithPassword(sshUser, sshPass);
            if (login) {
                LOG.info(sshHost + " 连接成功");
                return connection;
            } else {
                throw new RuntimeException(sshHost + " 用户名密码不正确");
            }
        } catch (Exception e) {
            throw new RuntimeException(sshHost + " " + e);
        }
    }


    /**
     * 执行Shell命令（使用EXEC方式）
     * <p>
     * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭Session，不会产生阻塞。
     * </p>
     *
     * @param connection 连接
     * @param command    命令
     * @param charset    发送和读取内容的编码
     * @return 执行返回结果
     */
    public static String execCommand(Connection connection, String command, String charset) {
        final String result;
        final String stderrStr;
        ByteArrayOutputStream errStream = new ByteArrayOutputStream();
        Session session = null;
        try {
            session = connection.openSession();
            Charset charset1 = Charset.forName(charset);
            session.execCommand(command, charset1.name());
            result = IoUtil.read(new StreamGobbler(session.getStdout()), charset1);
            //打印错误的流输出
            IoUtil.copy(new StreamGobbler(session.getStderr()), errStream);
            stderrStr = new String(errStream.toByteArray(), charset);
            if (!StrUtil.isEmpty(stderrStr)) {
                return null;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }


    /**
     * 执行Shell命令
     * <p>
     * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭Session，不会产生阻塞。
     * </p>
     *
     * @param cmd       命令
     * @param charset   发送和读取内容的编码
     * @param errStream 错误信息输出到的位置
     * @return 执行返回结果
     */
    public static String execByShell(String cmd, Charset charset, OutputStream errStream) {
        final String result;
        Session session = null;
        try {
            //这个方法有问题
            session.requestDumbPTY();
            IoUtil.write(session.getStdin(), charset, true, cmd);

            result = IoUtil.read(new StreamGobbler(session.getStdout()), charset);
            if (null != errStream) {
                // 错误输出
                IoUtil.copy(new StreamGobbler(session.getStdout()), errStream);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(session);
        }
        return result;
    }

    /**
     * 关闭会话
     */
    public static void close(Session session) {

        if (session != null) {
            session.close();
        }
    }

    /**
     * 关闭会话
     */
    public void closeConnect(Connection connection) {

        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        Connection connection = SSH2Utils.openConnection("bigdata1", 22021, "shinow", "shinow" );

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Charset charset = Charset.forName("UTF-8");
        String ls = SSH2Utils.execCommand(connection, "ls", "UTF-8");


        String str = new String(byteArrayOutputStream.toByteArray(), charset);
        System.out.println(ls);
        System.out.println("error=" + str);

        long currentTimeMillis1 = System.currentTimeMillis();
        System.out.println("Ganymed方式" + (currentTimeMillis1 - System.currentTimeMillis()));
    }

}
