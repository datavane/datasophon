import { ModalForm, ModalFormProps, ProFormRadio, ProFormSelect, ProFormText, ProFormTextArea } from "@ant-design/pro-components";
import { useTranslation } from "react-i18next";
import { APIS } from "../../../services/cluster";
import { useParams } from "react-router-dom";
import { useState } from "react";

interface ModalType extends ModalFormProps {
    data?: any;
    onAlertGroupIdChange: (value: string) => void;
}
const AlarmMetricsModal = (props: ModalType) => {
    const { t } = useTranslation()
    return (
        <ModalForm
            { ...props}
        >
            <ProFormText
                name="alertQuotaName"
                label="告警指标名称"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormText>
            <ProFormText
                name="alertExpr"
                label="指标表达式"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormText>
            <ProFormSelect
                name="compareMethod"
                label="比较方式"
                rules={[
                    {
                      required: true,
                    },
                  ]}
                options={[
                    {
                        label: "!=",
                        value: "!=",
                      },
                      {
                        label: ">",
                        value: ">",
                      },
                      {
                        label: "<",
                        value: "<",
                      },
                ]}
            ></ProFormSelect>
            <ProFormText
                name="alertThreshold"
                label="告警阀值"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormText>
            <ProFormSelect
                name="alertLevel"
                label="告警级别"
                rules={[
                    {
                      required: true,
                    },
                  ]}
                options={[
                    {
                        label: "警告",
                        value: "warning",
                      },
                      {
                        label: "异常",
                        value: "exception",
                      },
                ]}
            ></ProFormSelect>
             <ProFormSelect
                name="alertGroupId"
                label="告警组"
                rules={[
                    {
                      required: true,
                    },
                  ]}
                options={props.data?.alarmGroup || []}
                onChange={(value: string) => {
                  props.onAlertGroupIdChange(value)
                }}
            ></ProFormSelect>
            <ProFormSelect
                name="serviceRoleName"
                label="绑定角色"
                rules={[
                    {
                      required: true,
                    },
                ]}
                options={props.data?.serviceRoleName || []}
            ></ProFormSelect>
            <ProFormSelect
                name="noticeGroupId"
                label="通知组"
                rules={[
                    {
                      required: true,
                    },
                ]}
                  options={[
                    {
                        label: "数据开发组",
                        value: 1,
                    },
                ]}
            ></ProFormSelect>
            <ProFormRadio.Group
                name="alertTactic"
                label="告警策略"
                initialValue="1"
                rules={[
                  {
                    required: true,
                  },
                ]}
                options={[
                    {
                        label: '单次',
                        value: '1',
                    },
                    {
                        label: '连续',
                        value: '2',
                    },
                ]}
          />
           <ProFormText
                name="intervalDuration"
                label="间隔时长(分钟)"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormText>
            <ProFormText
                name="triggerDuration"
                label="触发时长(秒)"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormText>
            <ProFormTextArea
                name="alertAdvice"
                label="告警建议"
                rules={[
                    {
                      required: true,
                    },
                  ]}
            ></ProFormTextArea>
        </ModalForm>
    )
}

export default AlarmMetricsModal