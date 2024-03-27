package com.ssafy.maryflower.bouquet.exception;

import com.ssafy.maryflower.global.exception.ErrorCode;
import com.ssafy.maryflower.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class BouquetException extends GlobalException {

    public BouquetException(BouquetErrorCode  errorCode) {
        super(errorCode);
    }
}
