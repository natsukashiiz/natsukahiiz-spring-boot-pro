package com.natsukashiiz.iicommon.model;

import lombok.Data;

/**
 * {
 *    "page": 1,
 *    "limit": 10
 * }
 */
@Data
public class Pagination {
    private Integer page = 0;
    private Integer limit = 10;
}
