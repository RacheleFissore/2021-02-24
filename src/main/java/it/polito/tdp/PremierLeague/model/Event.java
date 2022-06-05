package it.polito.tdp.PremierLeague.model;

import java.time.Duration;

public class Event implements Comparable<Event>{
	// Tipologie di eventi possibili
	public enum EventType {
		GOAL,
		ESPULSIONE,
		INFORTUNIO
	}
	
	private EventType type;
	private Duration durata;
	private Integer team;

	public Event(EventType type, Duration durata, Integer team) {
		super();
		this.type = type;
		this.durata = durata;
		this.team = team;
	}

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public Duration getDurata() {
		return durata;
	}

	public void setDurata(Duration durata) {
		this.durata = durata;
	}

	public Integer getTeam() {
		return team;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}

	@Override
	public int compareTo(Event o) {
		return this.durata.compareTo(o.durata);
	}
}
