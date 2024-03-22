package org.jsp.eventmanagementsystem.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
	private String event_name, city_name, weather;
	private LocalDate date;
	private double distance_km;
}
