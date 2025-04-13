package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.service;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.request.SendChatRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.ChatInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.RoomListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.SendChatResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception.ChatMessageException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.repository.ChatMessageRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
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

import static com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception.ChatMessageExceptions.MESSAGE_NOT_FOUND;
import static com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingExceptions.MEETING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private static final Long DEFAULT_SSE_TIMEOUT = 60L * 1000 * 60;
    private final MeetingRepository meetingRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final Map<Meeting, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public RoomListResponse getRoomList(User user) {

        List<RoomListResponse.Room> rooms = meetingRepository.findAllByParticipantsContaining(user)
                .stream()
                .map(meeting -> RoomListResponse.Room.builder()
                        .meeting(meeting)
                        .last(chatMessageRepository.findLastByMeeting(meeting).orElse(null))
                        .build())
                .toList();

        return RoomListResponse.builder()
                .rooms(rooms)
                .build();
    }

    public ChatInfoResponse getChatInfo(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        return ChatInfoResponse.builder()
                .participants(meeting.getParticipants())
                .messages(chatMessageRepository.findAllByMeeting(meeting))
                .build();
    }

    public SendChatResponse sendChat(User user, String meetingId, @Valid SendChatRequest dto) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        ChatMessage chatMessage = chatMessageRepository.save(
                ChatMessage.builder()
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
                                .id(chatMessage.getId())
                                .name("send_chat")
                                .data(chatMessage)
                );
            } catch (IOException ignored) {
                emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter);
            }
        });

        return SendChatResponse.builder()
                .message(chatMessage)
                .build();
    }

    public void readAllChat(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
                .orElseThrow(() -> MeetingException.of(MEETING_NOT_FOUND));

        chatMessageRepository.findAllByMeeting(meeting).forEach(chatMessage -> {
            chatMessage.getUnread().remove(user);
            chatMessageRepository.save(chatMessage);
        });
    }

    public void readChat(User user, String chattingId) {

        ChatMessage chatMessage = chatMessageRepository.findById(chattingId)
                .filter(x -> x.getMeeting().getParticipants().contains(user))
                .orElseThrow(() -> ChatMessageException.of(MESSAGE_NOT_FOUND));

        chatMessage.getUnread().remove(user);

        chatMessageRepository.save(chatMessage);
    }

    public SseEmitter openSseTunnel(User user, String meetingId) {

        Meeting meeting = meetingRepository.findById(meetingId)
                .filter(x -> x.getParticipants().contains(user))
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
