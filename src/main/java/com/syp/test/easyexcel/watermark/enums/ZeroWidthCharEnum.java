package com.syp.test.easyexcel.watermark.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by shiyuping on 2024/5/7 16:48
 *
 * @author shiyuping
 */
@AllArgsConstructor
@Getter
public enum ZeroWidthCharEnum {

    /**/
    ZERO_WIDTH_CHAR_0("\u202C", 0),
    ZERO_WIDTH_CHAR_1("\u202D", 1);


    private final String zeroWidthChar;

    private final Integer num;


    public static String getZeroWidthChar(Integer num) {
        for (ZeroWidthCharEnum value : ZeroWidthCharEnum.values()) {
            if (value.getNum().equals(num)) {
                return value.getZeroWidthChar();
            }
        }
        return ZERO_WIDTH_CHAR_0.getZeroWidthChar();
    }

    public static Integer getNum(String zeroWidthChar) {
        for (ZeroWidthCharEnum value : ZeroWidthCharEnum.values()) {
            if (value.getZeroWidthChar().equals(zeroWidthChar)) {
                return value.getNum();
            }
        }
        return ZERO_WIDTH_CHAR_0.getNum();
    }
}
