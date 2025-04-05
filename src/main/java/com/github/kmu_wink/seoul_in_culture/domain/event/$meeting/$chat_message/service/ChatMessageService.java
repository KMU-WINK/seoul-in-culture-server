package com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.request.SendChatRequest;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.ChatInfoResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.RoomListResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.dto.response.SendChatResponse;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception.ChatMessageNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.exception.NotJoiningRoomException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.repository.ChatMessageRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$chat_message.schema.ChatMessage;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.repository.MeetingParticipantRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.$participant.schema.MeetingParticipant;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.exception.MeetingNotFoundException;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.repository.MeetingRepository;
import com.github.kmu_wink.seoul_in_culture.domain.event.$meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final MeetingRepository meetingRepository;
	private final MeetingParticipantRepository meetingParticipantRepository;
	private final ChatMessageRepository chatMessageRepository;

	private final Map<Meeting, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

	private static final Long DEFAULT_SSE_TIMEOUT = 60L * 1000 * 60;

	public RoomListResponse getRoomList(User user) {

		List<RoomListResponse.Room> rooms = meetingParticipantRepository.findAllByUser(user).stream()
			.map(MeetingParticipant::getMeeting)
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

		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(MeetingNotFoundException::new);

		if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) throw new NotJoiningRoomException();

		List<User> participants = meetingParticipantRepository.findAllByMeeting(meeting).stream()
			.map(MeetingParticipant::getUser)
			.toList();

		List<ChatMessage> messages = chatMessageRepository.findAllByMeeting(meeting);

		return ChatInfoResponse.builder()
			.participants(participants)
			.messages(messages)
			.build();
	}

	public SendChatResponse sendChat(User user, String meetingId, @Valid SendChatRequest dto) {

		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(MeetingNotFoundException::new);

		if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) throw new NotJoiningRoomException();

		ChatMessage chatMessage = chatMessageRepository.save(
			ChatMessage.builder()
				.meeting(meeting)
				.user(user)
				.content(dto.content())
				.read(Set.of())
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

		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(MeetingNotFoundException::new);

		if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) throw new NotJoiningRoomException();

		chatMessageRepository.findAllByMeeting(meeting).forEach(chatMessage -> {

			chatMessage.getRead().add(user);

			chatMessageRepository.save(chatMessage);
		});
	}

	public void readChat(User user, String chattingId) {

		ChatMessage chatMessage = chatMessageRepository.findById(chattingId).orElseThrow(ChatMessageNotFoundException::new);

		if (!meetingParticipantRepository.existsByMeetingAndUser(chatMessage.getMeeting(), user)) throw new NotJoiningRoomException();

		chatMessage.getRead().add(user);

		chatMessageRepository.save(chatMessage);
	}

	public SseEmitter openSseTunnel(User user, String meetingId) {

		Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(MeetingNotFoundException::new);

		if (!meetingParticipantRepository.existsByMeetingAndUser(meeting, user)) throw new NotJoiningRoomException();

		SseEmitter emitter = new SseEmitter(DEFAULT_SSE_TIMEOUT);

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

		emitter.onCompletion(() -> emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter));
		emitter.onTimeout(() -> emitters.getOrDefault(meeting, Collections.emptyList()).remove(emitter));

		emitters.computeIfAbsent(meeting, x -> new CopyOnWriteArrayList<>()).add(emitter);

		return emitter;
	}
}
