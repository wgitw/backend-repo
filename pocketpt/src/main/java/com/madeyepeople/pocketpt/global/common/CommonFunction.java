package com.madeyepeople.pocketpt.global.common;

import com.madeyepeople.pocketpt.global.error.ErrorCode;
import com.madeyepeople.pocketpt.global.error.exception.BusinessException;
import com.madeyepeople.pocketpt.global.error.exception.CustomExceptionMessage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonFunction {
    public static Date convertStringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = null;
        try {
            convertedDate = format.parse(date);
        } catch (ParseException e) {
            throw new BusinessException(ErrorCode.HISTORICAL_DATA_ERROR, CustomExceptionMessage.INVALID_DATE_FORMAT.getMessage());
        }
        return convertedDate;
    }
}
