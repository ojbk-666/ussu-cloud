package cc.ussu.modules.sheep.task.jd.vo.fruit.response;

import cc.ussu.modules.sheep.task.jd.vo.JdBaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class FriendListInitForFarmResponse extends JdBaseResponse {

    private String shareCodeInviteNotFarmAddOn;
    private String shareCodeCallAddOn;
    private String inviteFriendCountToPropsSendCard;
    private Integer inviteFriendCountToProps;
    private Integer inviteFriendGotAwardCount;
    private Integer inviteFriendCount;
    private Integer inviteFirstFriendAwardWater;
    private Integer awardCallUserWater;
    private String shareCodeInviteAddOn;
    private Boolean hadGotInviteFriendCountToProps;
    private Long sysTime;
    private Boolean fullFriend;
    private Integer inviteFriendAwardEach;
    private Integer countOfFriend;
    private List<Friend> friends;
    private Boolean newUserAward;
    private Integer deleteFriendCount;
    private Integer awardInviteNewUserSendWater;
    private Integer inviteFriendMax;
    private Boolean newFriendMsg;
    private Boolean loadFriend;

    @Data
    public static class Friend {
        private String id;
        private String nickName;
        /**
         * 头像
         */
        private String imageUrl;
        /**
         * 助力码
         */
        private String shareCode;
        private Integer friendState;
    }

}
