package com.mycompany.myapp.domain;

import java.time.LocalDateTime;

public class RegistrationDetails {

    private Long lessonInstanceId;
    private Long studentId;
    private boolean attended;
    private LocalDateTime createdAt;

    public RegistrationDetails(Long lessonInstanceId, Long studentId, boolean attended, LocalDateTime createdAt) {
        this.lessonInstanceId = lessonInstanceId;
        this.studentId = studentId;
        this.attended = attended;
        this.createdAt = createdAt;
    }

    public Long getLessonInstanceId() {
        return lessonInstanceId;
    }

    public void setLessonInstanceId(Long lessonInstanceId) {
        this.lessonInstanceId = lessonInstanceId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public boolean isAttended() {
        return attended;
    }

    public void setAttended(boolean attended) {
        this.attended = attended;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
