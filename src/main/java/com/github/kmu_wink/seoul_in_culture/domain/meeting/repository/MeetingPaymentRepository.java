package com.github.kmu_wink.seoul_in_culture.domain.meeting.repository;

import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.Meeting;
import com.github.kmu_wink.seoul_in_culture.domain.meeting.schema.MeetingPayment;
import com.github.kmu_wink.seoul_in_culture.domain.user.schema.User;

import java.util.Optional;

public interface MeetingPaymentRepository {

    Optional<MeetingPayment> findByUserAndMeeting(User user, Meeting meeting);

    MeetingPayment save(MeetingPayment meetingPayment);

    void delete(MeetingPayment meetingPayment);
}