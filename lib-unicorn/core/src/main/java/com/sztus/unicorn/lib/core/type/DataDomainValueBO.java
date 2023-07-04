package com.sztus.unicorn.lib.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据域值
 *
 * @author Jay
 * @date 2022/09/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDomainValueBO {

    private Long id;

    private Long domainId;

    private Integer sn;

    private String code;

    private String value;

    private String text;

    private String extension;

    private Integer status;

    private Long createdAt;

    private Long updatedAt;
}
