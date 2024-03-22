package org.jsp.eventmanagementsystem.controller;

import java.time.LocalDate;

import org.jsp.eventmanagementsystem.dto.EventResponse;
import org.jsp.eventmanagementsystem.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
	@Autowired
	private EventService eventService;

	@PostMapping
	public String getPath() {
		return eventService.addEvents();
	}

	@GetMapping("/find")
	public EventResponse findEvents(@RequestParam double longitude, @RequestParam double latitude,
			@RequestParam LocalDate date) {
		return eventService.findEvents(date, longitude, latitude);
	}
}
