package org.jsp.eventmanagementsystem.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import org.jsp.eventmanagementsystem.dto.DistanceDto;
import org.jsp.eventmanagementsystem.dto.EventDto;
import org.jsp.eventmanagementsystem.dto.EventResponse;
import org.jsp.eventmanagementsystem.dto.WeatherDto;
import org.jsp.eventmanagementsystem.model.Event;
import org.jsp.eventmanagementsystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EventService {
	@Value("${file.path}")
	private String path;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private WebClient webClient;

	public String addEvents() {
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			String line;
			boolean l = true;
			while ((line = br.readLine()) != null) {
				if (l) {
					l = false;
					continue;
				}
				String[] data = line.split(",");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				Event event = new Event();
				event.setEventName(data[0]);
				event.setCityName(data[1]);
				event.setDate(LocalDate.parse(data[2], formatter));
				event.setTime(LocalTime.parse(data[3]));
				event.setLatitude(Double.parseDouble(data[4]));
				event.setLongitude(Double.parseDouble(data[5]));
				eventRepository.save(event);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Events added";
	}

	public WeatherDto getWeather(String city, LocalDate date) {
		return webClient.get().uri(
				"https://gg-backend-assignment.azurewebsites.net/api/Weather?code=KfQnTWHJbg1giyB_Q9Ih3Xu3L9QOBDTuU5zwqVikZepCAzFut3rqsg==",
				uriBuilder -> uriBuilder.queryParam("city", city).queryParam("date", date).build()).retrieve()
				.bodyToMono(WeatherDto.class).block();
	}

	public EventResponse findEvents(LocalDate startDate, double longitude, double latitude) {
		LocalDate endDate = startDate.plusDays(14);
		Page<Event> eventPage = eventRepository.findByDateBetweenOrderByDateAsc(startDate, endDate,
				PageRequest.of(1, 10));

		List<Event> events = eventPage.getContent();
		System.out.println(events.size());
		List<EventDto> eventDtos = events.stream().map(event -> mapToEventDto(event, longitude, latitude)).toList();
		EventResponse eventResponse = new EventResponse();
		eventResponse.setEvents(eventDtos);
		eventResponse.setPage(eventPage.getNumber());
		eventResponse.setPageSize(eventPage.getSize());
		eventResponse.setTotalEvents(eventPage.getTotalElements());
		eventResponse.setTotalPages(eventPage.getTotalPages());
		return eventResponse;
	}

	public EventDto mapToEventDto(Event event, double longitude, double latitude) {
		double distance = webClient.get().uri(
				"https://gg-backend-assignment.azurewebsites.net/api/Distance?code=IAKvV2EvJa6Z6dEIUqqd7yGAu7IZ8gaH-a0QO6btjRc1AzFu8Y3IcQ==",
				uriBuilder -> uriBuilder.queryParam("latitude1", latitude).queryParam("longitude1", longitude)
						.queryParam("latitude2", event.getLatitude()).queryParam("longitude2", event.getLongitude())
						.build())
				.retrieve().bodyToMono(DistanceDto.class).block().getDistance();
		return EventDto.builder().distance_km(distance)
				.weather(getWeather(event.getCityName(), event.getDate()).getWeather()).city_name(event.getCityName())
				.event_name(event.getEventName()).date(event.getDate()).build();
	}
}
