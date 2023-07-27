package com.natsukashiiz.iicommon.model;

import com.natsukashiiz.iicommon.common.DeviceCode;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Http {
    private String ua;
    private String ipv4;
    private DeviceCode device;
}
