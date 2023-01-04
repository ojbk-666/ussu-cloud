package cc.ussu.modules.sheep.task.jd.vo.speedcoin.invite.response;

import cc.ussu.modules.sheep.task.jd.vo.speedcoin.response.clienthandleservice.BaseClientHandleServiceExecuteResponse;
import lombok.Data;

@Data
public class InfoResponse extends BaseClientHandleServiceExecuteResponse {

    private InvitationTaskVO data;

    @Data
    public static class InvitationTaskVO {
        /**
         * 输入的邀请码
         */
        private String inviterCode;

        private String inviteTotalGoldAccumulate;

        private String yqyActivityRule;

        private String invitePredictMoney;

        private String suspensionLink;

        private Integer jklWxInviteSwitch;

        /**
         * 邀请码
         */
        private String encryptionInviterPin;

        private Integer inviteAccumulate;

        private String jklPopupPic;

        private String h5ShareSubtitle;

        private String inviterNick;

        private Integer inviteGoldAccumulate;

        private String yqyEarnMoneyPic;

        private String h5SharePic;

        private String inviterHead;

        private String h5ShareMainTitle;

        private String inviteMdmMarker;

        private String suspensionFrame;

        private Integer jklQqInviteSwitch;

        private String suspensionOpen;

        private String jklInviteText;

        private Integer jklPyqInviteSwitch;

        private String yqyTopTitlePic;

        private String jklPopupTitle;
    }

}
