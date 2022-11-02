package com.datasophon.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class CompressUtils {
    private static final Logger logger = LoggerFactory.getLogger(CompressUtils.class);

    public static void main(String[] args) throws IOException {
        decompressTarGz("D:\\DDP\\apache-druid-0.20.2-bin.tar.gz","D:\\360downloads");
    }


    public static Boolean decompressTarGz(String sourceTarGzFile, String targetDir) throws IOException {
//        if(!sourceTarGzFile.contains("trino-367")){
            logger.info("use tar -zxvf to decompress");
            ArrayList<String> command = new ArrayList<>();
            command.add("tar");
            command.add("-zxvf");
            command.add(sourceTarGzFile);
            command.add("-C");
            command.add(targetDir);
            ExecResult execResult = ShellUtils.execWithStatus(targetDir, command, 120);
            return execResult.getExecResult();
//        }
//        File sourTarGz = new File(sourceTarGzFile);
//        logger.info("use jar  to decompress");
//        // decompressing *.tar.gz files to tar
//        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(new GzipCompressorInputStream(Files.newInputStream(sourTarGz.toPath())));
//        TarArchiveEntry entry;
//        // 将 tar 文件解压到 targetDir 目录下
//        // 将 tar.gz文件解压成tar包,然后读取tar包里的文件元组，复制文件到指定目录
//
//        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
//            if (entry.isDirectory()) {
//                continue;
//            }
//
//            File targetFile = new File(targetDir, entry.getName());
//            File parent = targetFile.getParentFile();
//            if (!parent.exists()) {
//                parent.mkdirs();
//            }
//            logger.info(entry.getName());
//            // 将文件写出到解压的目录
//            OutputStream outputStream = Files.newOutputStream(targetFile.toPath());
//            IoUtil.copy(tarArchiveInputStream, outputStream);
//            outputStream.close();
//        }
//        if(Objects.nonNull(tarArchiveInputStream)){
//            tarArchiveInputStream.close();
//        }
//        return true;
    }


}
