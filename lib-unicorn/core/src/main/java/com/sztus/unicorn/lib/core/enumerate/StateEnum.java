package com.sztus.unicorn.lib.core.enumerate;

import com.sztus.unicorn.lib.core.base.BaseEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fly_Roushan
 * @date 2021/1/11
 */
public enum StateEnum implements BaseEnum {
    AK(1, "AK"),
    AL(2, "AL"),
    AR(3, "AR"),
    AZ(4, "AZ"),
    CA(5, "CA"),
    CO(6, "CO"),
    CT(7, "CT"),
    DC(8, "DC"),
    DE(9, "DE"),
    FL(10, "FL"),
    GA(11, "GA"),
    HI(12, "HI"),
    IA(13, "IA"),
    ID(14, "ID"),
    IL(15, "IL"),
    IN(16, "IN"),
    KS(17, "KS"),
    KY(18, "KY"),
    LA(19, "LA"),
    MA(20, "MA"),
    MD(21, "MD"),
    ME(22, "ME"),
    MI(23, "MI"),
    MN(24, "MN"),
    MO(25, "MO"),
    MS(26, "MS"),
    MT(27, "MT"),
    NC(28, "NC"),
    ND(29, "ND"),
    NE(30, "NE"),
    NH(31, "NH"),
    NJ(32, "NJ"),
    NM(33, "NM"),
    NV(34, "NV"),
    NY(35, "NY"),
    OH(36, "OH"),
    OK(37, "OK"),
    OR(38, "OR"),
    PA(39, "PA"),
    PR(40, "PR"),
    RI(41, "RI"),
    SC(42, "SC"),
    SD(43, "SD"),
    TN(44, "TN"),
    TX(45, "TX"),
    UT(46, "UT"),
    VA(47, "VA"),
    VT(48, "VT"),
    WA(49, "WA"),
    WI(50, "WI"),
    WV(51, "WV"),
    WY(52, "WY");


    StateEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getText() {
        return this.text;
    }

    private static final Map<String, StateEnum> stateMappingContainer = new HashMap<>();
    private static final Map<Integer, StateEnum> valueMappingContainer = new HashMap<>();

    static {
        for (StateEnum stateEnum : StateEnum.values()) {
            stateMappingContainer.put(stateEnum.getText(), stateEnum);
            valueMappingContainer.put(stateEnum.getValue(), stateEnum);
        }
    }

    private Integer value;
    private String text;

    public static StateEnum getStateEnumBytext(String state) {
        return stateMappingContainer.get(state);
    }

    public static StateEnum getStateEnumByValue(Integer value) {
        return valueMappingContainer.get(value);
    }
}
