package com.room.reservation.repository;


import com.room.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface RoomRepository extends JpaRepository<Room, Long>, QuerydslPredicateExecutor<Room> {

}
