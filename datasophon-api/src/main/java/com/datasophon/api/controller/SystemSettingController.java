package com.datasophon.api.controller;

import com.datasophon.api.service.SystemSettingService;
import com.datasophon.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "系统设置", value = "System Setting")
@RestController
@RequestMapping("system/setting")
public class SystemSettingController {

    @Autowired
    SystemSettingService systemSettingService;

    /**
     * 根据服务名称查询服务配置选项
     */
    @GetMapping("/powerOn/{isPowerOn}")
    @ApiOperation(value = "powerOn", notes = "POWER_ON")
    public Result getServiceConfigOption(@PathVariable("isPowerOn") Boolean isPowerOn) throws IOException {
        systemSettingService.powerOn(isPowerOn);
        return Result.success();
    }
}
