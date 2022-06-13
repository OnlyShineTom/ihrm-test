package com.ihrm.common.exception;

import com.ihrm.common.entity.ResultCode;
import lombok.Getter;


//这是我们的自定义异常

@Getter
public class CommonException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private ResultCode code = ResultCode.SERVER_ERROR;

    public CommonException(){}
    public CommonException(ResultCode resultCode) {
        super(resultCode.message());
        this.code = resultCode;
    }

}
