package cn.ussu.common.core.exception;

import cn.ussu.common.core.constants.ErrorCodeConstants;
import cn.ussu.common.core.constants.ErrorMsgConstants;

public class UsernamePasswordInvalidException extends ServiceException{

    public UsernamePasswordInvalidException() {
        super(ErrorCodeConstants.ACC_PWD_ERROR ,ErrorMsgConstants.ACC_PWD_ERROR);
    }

    public UsernamePasswordInvalidException(String errorMessage) {
        super(ErrorCodeConstants.ACC_PWD_ERROR, errorMessage);
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return null;
    }
}
