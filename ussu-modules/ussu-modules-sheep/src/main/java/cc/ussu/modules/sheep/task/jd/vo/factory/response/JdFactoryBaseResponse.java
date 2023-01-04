package cc.ussu.modules.sheep.task.jd.vo.factory.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class JdFactoryBaseResponse<T> implements Serializable {

    private static final long serialVersionUID = -2527836326762544068L;

    private Integer code;

    private String msg;

    private JdFactoryBaseResponseData<T> data;

    public boolean isOk() {
        return 0 == code && data != null;
    }

    public <T> T getResult() {
        if (isOk() && data.isOk()) {
            return data.getResult();
        }
        return null;
    }

    @Data
    public static class JdFactoryBaseResponseData<T> {

        private Integer bizCode;
        private boolean success;
        private String bizMsg;
        private T result;

        public boolean isOk() {
            return success;
        }

        public <T> T getResult() {
            return (T) result;
        }
    }

}
