package com.datasophon.api.controller;

import com.datasophon.api.service.ClusterKerberosService;
import com.datasophon.common.utils.Result;
import com.datasophon.dao.entity.ClusterNodeLabelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("cluster/kerberos")
public class ClusterKerberosController {

    @Autowired
    private ClusterKerberosService kerberosService;

    /**
     * download keytab
     */
    @GetMapping("/downloadKeytab")
    public void list(Integer clusterId,String principal,String keytabName, HttpServletResponse response) throws IOException {
        kerberosService.downloadKeytab(clusterId,principal,keytabName,response);
    }
}
