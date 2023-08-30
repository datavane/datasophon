package com.datasophon.common.command;

/**
 * @Enum OlapOpsType
 * @Author: 张大伟
 * @Date: 2023/4/24 21:28
 * @Version: 1.0
 */
public enum OlapOpsType {

    ADD_BE(1, "backend"),
    ADD_FE_FOLLOWER(1, "follower"),
    ADD_FE_OBSERVER(1, "observer");

    private int value;

    private String desc;

    OlapOpsType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
