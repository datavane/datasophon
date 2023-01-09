package com.datasophon.api.service.strategy;

import com.datasophon.api.utils.ProcessUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.model.ServiceConfig;
import com.datasophon.common.utils.PlaceholderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class KerberosHandlerAbstract {
    public void removeConfigWithKerberos(List<ServiceConfig> list, Map<String, ServiceConfig> map, List<ServiceConfig> configs) {
        for (ServiceConfig serviceConfig : configs) {
            if(serviceConfig.isConfigWithKerberos()){
                if(map.containsKey(serviceConfig.getName())){
                    list.remove(map.get(serviceConfig.getName()));
                }
            }
        }
    }

    public void addConfigWithKerberos(Map<String, String> globalVariables, Map<String, ServiceConfig> map, List<ServiceConfig> configs, ArrayList<ServiceConfig> kbConfigs) {
        for (ServiceConfig serviceConfig : configs) {
            if(serviceConfig.isConfigWithKerberos()){
                if(map.containsKey(serviceConfig.getName())){
                    ServiceConfig config = map.get(serviceConfig.getName());
                    config.setRequired(true);
                    config.setHidden(false);
                    if(Constants.INPUT.equals(config.getType())) {
                        String value = PlaceholderUtils.replacePlaceholders((String) serviceConfig.getValue(), globalVariables, Constants.REGEX_VARIABLE);
                        config.setValue(value);
                    }
                }else{
                    serviceConfig.setRequired(true);
                    serviceConfig.setHidden(false);
                    if(Constants.INPUT.equals(serviceConfig.getType())) {
                        String value = PlaceholderUtils.replacePlaceholders((String) serviceConfig.getValue(), globalVariables, Constants.REGEX_VARIABLE);
                        serviceConfig.setValue(value);
                    }
                    kbConfigs.add(serviceConfig);
                }

            }
        }
    }

    public boolean isEnableKerberos(Integer clusterId, Map<String, String> globalVariables, boolean enableKerberos, ServiceConfig config,String serviceName) {
        if( (Boolean) config.getValue()){
            enableKerberos = true;
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enable"+serviceName+"Kerberos}", "true");
        }else {
            ProcessUtils.generateClusterVariable(globalVariables, clusterId, "${enable"+serviceName+"Kerberos}", "false");
        }
        return enableKerberos;
    }
}
