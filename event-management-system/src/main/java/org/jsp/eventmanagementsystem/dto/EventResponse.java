package org.jsp.eventmanagementsystem.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EventResponse {
	private List<EventDto> events;
	private int page;
	private int pageSize;
	private long totalEvents;
	private int totalPages;
}
