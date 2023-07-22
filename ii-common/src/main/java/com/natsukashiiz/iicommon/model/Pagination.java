package com.natsukashiiz.iicommon.model;

import lombok.Data;

@Data
public class Pagination {
    private Integer page = 0;
    private Integer limit = 10;
}
