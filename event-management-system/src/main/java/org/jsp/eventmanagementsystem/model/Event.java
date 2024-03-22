package org.jsp.eventmanagementsystem.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Event {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String eventName;
	    private String cityName;
	    private LocalDate date;
	    private LocalTime time;
	    private double latitude;
	    private double longitude;

}
