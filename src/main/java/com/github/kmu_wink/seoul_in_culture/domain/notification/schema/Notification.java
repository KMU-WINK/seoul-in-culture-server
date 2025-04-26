package com.github.kmu_wink.seoul_in_culture.domain.notification.schema;

import com.github.kmu_wink.seoul_in_culture.common.mongo.BaseSchema;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.ChatMessageDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingHostDelegateDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingJoinDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingLeaveDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.MeetingReviewDetail;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.NotificationDetail;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.EnumSet;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document
public class Notification extends BaseSchema {

    Type type;

    NotificationDetail detail;

    @DBRef(lazy = true)
    @Indexed
    User user;

    String url;

    boolean unread;

    public static String createUrl(Type type, NotificationDetail notificationDetail) {

        return switch (type) {
            case MEETING_JOIN -> {
                MeetingJoinDetail detail = (MeetingJoinDetail) notificationDetail;
                yield String.format("/meeting/%s", detail.getMeeting().getId());
            }
            case MEETING_LEAVE -> {
                MeetingLeaveDetail detail = (MeetingLeaveDetail) notificationDetail;
                yield String.format("/meeting/%s", detail.getMeeting().getId());
            }
            case MEETING_HOST_DELEGATE -> {
                MeetingHostDelegateDetail detail = (MeetingHostDelegateDetail) notificationDetail;
                yield String.format("/meeting/%s", detail.getMeeting().getId());
            }
            case MEETING_REVIEW -> "/profile/review";
            case CHAT_MESSAGE -> {
                ChatMessageDetail detail = (ChatMessageDetail) notificationDetail;
                yield String.format("/chat?id=%s", detail.getMessage().getMeeting().getId());
            }
        };
    }

    public String getTitle() {

        return switch (type) {
            case MEETING_JOIN -> "모임에 새로운 참여자가 생겼습니다.";
            case MEETING_LEAVE -> "모임에 참여자가 나갔습니다.";
            case MEETING_HOST_DELEGATE -> "모임의 호스트가 변경되었습니다.";
            case MEETING_REVIEW -> "리뷰가 달렸습니다.";
            case CHAT_MESSAGE -> "새로운 메시지가 왔습니다";
        };
    }

    public String getBody() {

        return switch (type) {
            case MEETING_JOIN -> {
                MeetingJoinDetail detail = (MeetingJoinDetail) this.detail;
                yield String.format("%s에 %s님이 참여했습니다.", detail.getMeeting().getTitle(), detail.getUser().getNickname());
            }
            case MEETING_LEAVE -> {
                MeetingLeaveDetail detail = (MeetingLeaveDetail) this.detail;
                yield String.format("%s에 %s님이 나갔습니다.", detail.getMeeting().getTitle(), detail.getUser().getNickname());
            }
            case MEETING_HOST_DELEGATE -> {
                MeetingHostDelegateDetail detail = (MeetingHostDelegateDetail) this.detail;
                yield String.format(
                        "%s의 호스트가 %s님으로 변경되었습니다.",
                        detail.getMeeting().getTitle(),
                        detail.getMeeting().getHost().getNickname()
                );
            }
            case MEETING_REVIEW -> {
                MeetingReviewDetail detail = (MeetingReviewDetail) this.detail;
                yield String.format(
                        "%s의 %s님이 리뷰를 달았습니다.",
                        detail.getMeeting().getTitle(),
                        detail.getUser().getNickname()
                );
            }
            case CHAT_MESSAGE -> {
                ChatMessageDetail detail = (ChatMessageDetail) this.detail;
                yield String.format(
                        "[%s] %s: %s",
                        detail.getMessage().getMeeting().getTitle(),
                        detail.getMessage().getUser().getNickname(),
                        detail.getMessage().getContent()
                );
            }
        };
    }

    @RequiredArgsConstructor
    public enum Type {

        MEETING_JOIN(MeetingJoinDetail.class),
        MEETING_LEAVE(MeetingLeaveDetail.class),
        MEETING_HOST_DELEGATE(MeetingHostDelegateDetail.class),
        MEETING_REVIEW(MeetingReviewDetail.class),
        CHAT_MESSAGE(ChatMessageDetail.class),
        ;

        private final Class<? extends NotificationDetail> detail;

        public static Type fromDetail(NotificationDetail detail) {

            return EnumSet.allOf(Type.class)
                    .stream()
                    .filter(x -> x.detail.equals(detail.getClass()))
                    .findAny()
                    .orElseThrow();
        }
    }
}