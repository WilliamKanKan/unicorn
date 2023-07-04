package com.sztus.unicorn.lib.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据域
 *
 * @author Jay
 * @date 2022/09/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDomainBO {

    private Long id;

    private Long dictionaryId;

    private String domainCode;

    private String domainName;

    private String description;

    private Integer status;

    private Long createdAt;

    private Long updatedAt;
}
