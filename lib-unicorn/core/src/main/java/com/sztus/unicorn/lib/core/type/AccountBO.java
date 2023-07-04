package com.sztus.unicorn.lib.core.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author free
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountBO {

    private Long employeeId;

    private String openId;

    private Long departmentId;

    private Long occupationId;

    private String employeeNo;

    private String firstName;

    private String middleName;

    private String lastName;

    private String birthday;

    private String email;

    private String telephone;

    private String token;

    private String nickname;

    private String introduction;

    private String level;

    private String feature;

    private String category;

    private String departmentName;

    private String avatarUrl;

    /**
     * siteName
     */
    private String siteId;

    private String siteName;

    private Long companyId;

    private Long roleId;

    private String fullName;

    private String abbrName;

    private String description;

    private String roleCode;

    private List<Long> portfolioIdList;

    private List<Long> currentPortfolioIdList;

    private List<Long> tribeIdList;


}
