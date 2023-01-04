package cc.ussu.modules.sheep.task.jd.vo;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.Map;

@Data
public class JdBodyParam {

    private JSONObject jsonObject = new JSONObject();

    public JdBodyParam put(String key, Object value) {
        jsonObject.set(key, value);
        return this;
    }

    public JdBodyParam type(Integer type) {
        jsonObject.set("type", type);
        return this;
    }

    public JdBodyParam type(String type) {
        jsonObject.set("type", type);
        return this;
    }

    public JdBodyParam version(Integer version) {
        jsonObject.set("version", version);
        return this;
    }

    public JdBodyParam version(String version) {
        jsonObject.set("version", version);
        return this;
    }

    public JdBodyParam channel(Integer channel) {
        jsonObject.set("channel", channel);
        return this;
    }

    public JdBodyParam channel(String channel) {
        jsonObject.set("channel", channel);
        return this;
    }

    public JdBodyParam babelChannel(String babelChannel) {
        jsonObject.set("babelChannel", babelChannel);
        return this;
    }

    public JdBodyParam advertId(String advertId) {
        jsonObject.set("advertId", advertId);
        return this;
    }

    public JdBodyParam sid(String sid) {
        jsonObject.set("sid", sid);
        return this;
    }

    public JdBodyParam un_area(String un_area) {
        jsonObject.set("un_area", un_area);
        return this;
    }

    public JdBodyParam id(String id) {
        jsonObject.set("id", id);
        return this;
    }

    public JdBodyParam step(String step) {
        jsonObject.set("step", step);
        return this;
    }

    public JdBodyParam linkId(String linkId) {
        jsonObject.set("linkId", linkId);
        return this;
    }

    public JdBodyParam orderId(Long orderId) {
        jsonObject.set("orderId", orderId);
        return this;
    }

    public JdBodyParam orderQty(Integer orderQty) {
        jsonObject.set("orderQty", orderQty);
        return this;
    }

    public JdBodyParam prizeType(Integer prizeType) {
        jsonObject.set("prizeType", prizeType);
        return this;
    }

    public JdBodyParam shareCode(String shareCode) {
        jsonObject.set("shareCode", shareCode);
        return this;
    }

    public JdBodyParam cardType(String cardType) {
        jsonObject.set("cardType", cardType);
        return this;
    }

    public JdBodyParam imageUrl(String imageUrl) {
        jsonObject.set("imageUrl", imageUrl);
        return this;
    }

    public JdBodyParam nickName(String nickName) {
        jsonObject.set("nickName", nickName);
        return this;
    }

    public JdBodyParam taskId(String taskId) {
        jsonObject.set("taskId", taskId);
        return this;
    }
    public JdBodyParam taskId(Integer taskId) {
        jsonObject.set("taskId", taskId);
        return this;
    }

    public JdBodyParam taskType(String taskType) {
        jsonObject.set("taskType", taskType);
        return this;
    }

    public JdBodyParam taskToken(String taskToken) {
        jsonObject.set("taskToken", taskToken);
        return this;
    }

    public JdBodyParam itemId(String itemId) {
        jsonObject.set("itemId", itemId);
        return this;
    }

    public JdBodyParam inviteType(String inviteType) {
        jsonObject.set("inviteType", inviteType);
        return this;
    }

    public JdBodyParam inviterPin(String inviterPin) {
        jsonObject.set("inviterPin", inviterPin);
        return this;
    }

    public JdBodyParam method(String method) {
        jsonObject.set("method", method);
        return this;
    }

    public JdBodyParam activeId(String activeId) {
        jsonObject.set("activeId", activeId);
        return this;
    }

    public JdBodyParam activeType(Integer activeType) {
        jsonObject.set("activeType", activeType);
        return this;
    }

    public JdBodyParam activeType(String activeType) {
        jsonObject.set("activeType", activeType);
        return this;
    }

    public JdBodyParam completionFlag(String completionFlag) {
        jsonObject.set("completionFlag", completionFlag);
        return this;
    }

    public JdBodyParam clientTime(Long clientTime) {
        jsonObject.set("clientTime", clientTime);
        return this;
    }

    public JdBodyParam clientTime(Float clientTime) {
        jsonObject.set("clientTime", clientTime);
        return this;
    }

    public JdBodyParam messageType(String messageType) {
        jsonObject.set("messageType", messageType);
        return this;
    }

    public JdBodyParam uuid(String uuid) {
        jsonObject.set("uuid", uuid);
        return this;
    }

    public JdBodyParam videoTimeLength(String videoTimeLength) {
        jsonObject.set("videoTimeLength", videoTimeLength);
        return this;
    }

    public JdBodyParam videoTimeLength(Integer videoTimeLength) {
        jsonObject.set("videoTimeLength", videoTimeLength);
        return this;
    }

    public JdBodyParam token(String token) {
        jsonObject.set("token", token);
        return this;
    }

    public JdBodyParam frontendInitStatus(String frontendInitStatus) {
        jsonObject.set("frontendInitStatus", frontendInitStatus);
        return this;
    }

    public JdBodyParam sign(String sign) {
        jsonObject.set("sign", sign);
        return this;
    }

    public JdBodyParam encryptProjectId(String encryptProjectId) {
        jsonObject.set("encryptProjectId", encryptProjectId);
        return this;
    }

    public JdBodyParam encryptAssignmentId(String encryptAssignmentId) {
        jsonObject.set("encryptAssignmentId", encryptAssignmentId);
        return this;
    }

    public JdBodyParam sourceCode(String sourceCode) {
        jsonObject.set("sourceCode", sourceCode);
        return this;
    }

    public JdBodyParam source(Integer source) {
        jsonObject.set("source", source);
        return this;
    }

    public JdBodyParam businessId(String businessId) {
        jsonObject.set("businessId", businessId);
        return this;
    }

    public JdBodyParam businessData(String businessData) {
        jsonObject.set("businessData", businessData);
        return this;
    }

    public JdBodyParam sceneid(String sceneid) {
        jsonObject.set("sceneid", sceneid);
        return this;
    }

    public JdBodyParam signStr(String signStr) {
        jsonObject.set("signStr", signStr);
        return this;
    }

    public JdBodyParam random(String random) {
        jsonObject.set("random", random);
        return this;
    }

    public JdBodyParam componentId(String componentId) {
        jsonObject.set("componentId", componentId);
        return this;
    }

    public JdBodyParam cpUid(String cpUid) {
        jsonObject.set("cpUid", cpUid);
        return this;
    }

    public JdBodyParam taskSDKVersion(String taskSDKVersion) {
        jsonObject.set("taskSDKVersion", taskSDKVersion);
        return this;
    }

    public JdBodyParam actId(String actId) {
        jsonObject.set("actId", actId);
        return this;
    }

    public JdBodyParam actionType(String actionType) {
        jsonObject.set("actionType", actionType);
        return this;
    }

    public Map<String, Object> getMap() {
        return jsonObject.getRaw();
    }

    public String getBody() {
        return jsonObject.toString();
    }

    @Override
    public String toString() {
        return getBody();
    }
}
