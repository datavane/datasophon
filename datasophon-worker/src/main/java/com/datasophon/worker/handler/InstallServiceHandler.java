package com.datasophon.worker.handler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.StreamProgress;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.datasophon.common.Constants;
import com.datasophon.common.utils.CompressUtils;
import com.datasophon.common.utils.ExecResult;
import com.datasophon.common.utils.PropertyUtils;
import com.datasophon.common.utils.ShellUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallServiceHandler {
    private static final Logger logger = LoggerFactory.getLogger(InstallServiceHandler.class);

    public ExecResult install(String packageName, String decompressPackageName, String packageMd5) {
        ExecResult execResult = new ExecResult();
        try {
            //安装包校验
            String destDir = Constants.INSTALL_PATH + Constants.SLASH + "DDP/packages" + Constants.SLASH;

            Boolean needDownLoad = true;

            if (FileUtil.exist(destDir + packageName)) {
                //文件存在，校验md5
                String md5cmd = "sh " + Constants.WORKER_SCRIPT_PATH + "md5.sh " + destDir + packageName;
                String md5 = ShellUtils.getPackageMd5(md5cmd);
                logger.info("packageMd5 is {}", packageMd5);
                logger.info("md5sum result is {}", md5);
                if (StringUtils.isNotBlank(md5) && packageMd5.trim().equals(md5.trim())) {
                    needDownLoad = false;
                }
            }
            String dest = destDir + packageName;
            if (needDownLoad) {
                String masterHost = PropertyUtils.getString(Constants.MASTER_HOST);
                String masterPort = PropertyUtils.getString(Constants.MASTER_WEB_PORT);

                String downloadUrl = "http://" + masterHost + ":" + masterPort + "/ddh/service/install/downloadPackage?packageName=" + packageName;

                logger.info("download url is {}", downloadUrl);

                HttpUtil.downloadFile(downloadUrl, FileUtil.file(dest), new StreamProgress() {
                    @Override
                    public void start() {
                        Console.log("开始下载。。。。");
                    }


                    @Override
                    public void progress(long progressSize, long l1) {
                        Console.log("已下载：{}", FileUtil.readableFileSize(progressSize));
                    }

                    @Override
                    public void finish() {
                        Console.log("下载完成！");
                    }
                });
                //校验md5
                execResult.setExecOut("download package " + packageName + "success");
                logger.info("download package {} success", packageName);
            }
            //解压安装包
            if (!FileUtil.exist(Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName)) {
                if (CompressUtils.decompressTarGz(dest, Constants.INSTALL_PATH)) {
                    execResult.setExecResult(true);
                    execResult.setExecOut("install package " + packageName + "success");
                    ShellUtils.exceShell(" chmod -R 755 " + Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName);
                    if ("prometheus-2.17.2".equals(decompressPackageName)) {
                        String alertPath = Constants.INSTALL_PATH + Constants.SLASH + decompressPackageName + Constants.SLASH + "alert_rules";
                        ShellUtils.exceShell("sed -i \"s/clusterIdValue/" + PropertyUtils.getString("clusterId") + "/g\" `grep clusterIdValue -rl " + alertPath + "`");
                    }
                } else {
                    execResult.setExecOut("install package " + packageName + "failed");
                    logger.info("install package " + packageName + " failed");
                }
            } else {
                execResult.setExecResult(true);
            }
        } catch (Exception e) {
            execResult.setExecOut(e.getMessage());
            e.printStackTrace();
        }
        return execResult;
    }
}
