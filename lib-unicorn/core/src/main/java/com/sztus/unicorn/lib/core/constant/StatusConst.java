package com.sztus.unicorn.lib.core.constant;

import java.util.Arrays;
import java.util.List;

public interface StatusConst {

    int UNKNOWN = 0;

    int DISABLED = -1;

    int ENABLED = 1;

    List<Integer> STATUS_LIST = Arrays.asList(UNKNOWN, DISABLED, ENABLED);
}
