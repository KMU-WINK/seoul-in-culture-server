package com.github.kmu_wink.seoul_in_culture.domain.chat.service;

import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.request.SendChatRequest;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.ChatInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.RoomListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.dto.response.SendChatResponse;
import com.github.kmu_wink.seoul_in_culture.domain.chat.exception.ChatException;
import com.github.kmu_wink.seoul_in_culture.domain.chat.repository.ChatRepository;
import com.github.kmu_wink.seoul_in_culture.domain.chat.schema.Chat;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.notification.api.NotificationApi;
import com.github.kmu_wink.seoul_in_culture.domain.notification.schema.detail.ChatMessageDetail;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.kmu_wink.seoul_in_culture.domain.chat.exception.ChatExceptions.MESSAGE_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.meeting.exception.MeetingExceptions.MEETING_NOT_JOINED;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MeetingRepository meetingRepository;
    private final ChatRepository chatRepository;

	private final NotificationApi notificationApi;

	private static final Map<Meeting, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

	private static final Long DEFAULT_SSE_TIMEOUT = 60L * 1000 * 60;

	public RoomListResponse getRoomList(User user) {

        List<RoomListResponse.Room> rooms = meetingRepository.findAllByParticipantsContaining(user)
                .stream()
                .map(meeting -> RoomListResponse.Room.builder()
                        .meeting(meeting)
                        .last(chatRepository.findLastByMeeting(meeting).orElse(null))
                        .build())
                .toList();

        return RoomListResponse.builder()
                .rooms(rooms)
                .build();
    }

    public ChatInfoResponse getChatInfo(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        return ChatInfoResponse.builder()
                .participants(meeting.getParticipants())
                .messages(chatRepository.findAllByMeeting(meeting))
                .build();
    }

    public SendChatResponse sendChat(User user, String meetingId, @Valid SendChatRequest dto) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        Chat chat = chatRepository.save(
                Chat.builder()
                        .meeting(meeting)
                        .user(user)
                        .content(dto.content())
                        .unread(meeting.getParticipants())
                        .build()
        );

        emitters.getOrDefault(meeting, Collections.emptyList()).forEach(emitter -> {

            try {
                emitter.send(
                        SseEmitter.event()
                                .id(chat.getId())
                                .name("send_chat")
                                .data(chat)
                );
            } catch (IOException ignored) {
                emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter);
            }
        });

		meeting.getParticipants().forEach(participant -> {
			if (participant.equals(user)) return;

			notificationApi.sendNotification(
					participant,
					ChatMessageDetail.builder()
							.message(chat)
							.build()
			);
		});

        return SendChatResponse.builder()
                .message(chat)
                .build();
    }

    public void readAllChat(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        chatRepository.findAllByMeeting(meeting).forEach(chatMessage -> {
            chatMessage.getUnread().remove(user);
            chatRepository.save(chatMessage);
        });
    }

    public void readChat(User user, String chattingId) {

        Chat chat = chatRepository.findById(chattingId)
                .stream()
                .peek(x -> {
                    if (!x.getMeeting().getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> ChatException.of(MESSAGE_NOT_FOUND));

        chat.getUnread().remove(user);

        chatRepository.save(chat);
    }

    public SseEmitter openSseTunnel(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .stream()
                .peek(x -> {
                    if (!x.getParticipants().contains(user))
                        throw MeetingException.of(MEETING_NOT_JOINED);
                })
                .findFirst()
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        SseEmitter emitter = new SseEmitter(DEFAULT_SSE_TIMEOUT);

        emitter.onCompletion(() -> emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter));
        emitter.onTimeout(() -> emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter));

        emitters.computeIfAbsent(meeting, x -> new CopyOnWriteArrayList<>()).add(emitter);

        try {
            emitter.send(
                    SseEmitter.event()
                            .id(UUID.randomUUID().toString())
                            .name("ping")
                            .build()
            );
        } catch (IOException e) {
            emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter);
        }

        return emitter;
    }
}
