package com.room.reservation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "room")
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyno;

    private String text;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;
}
