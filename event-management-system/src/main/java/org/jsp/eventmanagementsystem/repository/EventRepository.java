package org.jsp.eventmanagementsystem.repository;

import java.time.LocalDate;

import org.jsp.eventmanagementsystem.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
	public Page<Event> findByDateBetweenOrderByDateAsc(LocalDate startDate, LocalDate endDate, PageRequest pageRequest);
}
