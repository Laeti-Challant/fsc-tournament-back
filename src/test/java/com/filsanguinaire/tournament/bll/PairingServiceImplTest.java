package com.filsanguinaire.tournament.bll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.filsanguinaire.tournament.bo.Coach;
import com.filsanguinaire.tournament.bo.CoachStatus;
import com.filsanguinaire.tournament.bo.Event;
import com.filsanguinaire.tournament.bo.EventStatus;
import com.filsanguinaire.tournament.bo.Match;
import com.filsanguinaire.tournament.bo.PairingType;
import com.filsanguinaire.tournament.bo.Round;
import com.filsanguinaire.tournament.dal.CoachRepository;
import com.filsanguinaire.tournament.dal.EventRepository;
import com.filsanguinaire.tournament.dal.MatchRepository;
import com.filsanguinaire.tournament.dal.RoundRepository;
import com.filsanguinaire.tournament.exceptions.EventNotFoundException;
import com.filsanguinaire.tournament.exceptions.PairingException;
import com.filsanguinaire.tournament.exceptions.RoundNotFoundException;
import com.filsanguinaire.tournament.exceptions.TournamentNotEditableException;
import com.filsanguinaire.tournament.mapper.CoachMapper;

@ExtendWith(MockitoExtension.class)
public class PairingServiceImplTest {

	@Mock
	private RoundRepository roundRepository;
	
	@Mock
	private MatchRepository matchRepository;
	
	@Mock
	private CoachRepository coachRepository;
	
	@Mock
	private EventRepository eventRepository;
	
	@Mock
	private CoachMapper coachMapper;
	
	@Captor
	private ArgumentCaptor<List<Match>> matchesCaptor;
	
	@InjectMocks
	private PairingServiceImpl pairingService;
	
	private static final Long EVENT_ID = 1L;
	private static final Long ROUND_ID = 10L;
	
	@Test
	void shouldThrowRoundNotFoundExceptionWhenRoundNotFound() {
		// Arrange
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.empty());
		
		// Act and Assert
		assertThrows(RoundNotFoundException.class, () -> pairingService.generatePairings(EVENT_ID, ROUND_ID));
		verify(matchRepository, never()).saveAll(any());
	}
	
	@Test
	void shouldThrowEventNotFoundExceptionWhenEventNotFound() {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).build();
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.empty());
		
		// Act and Assert
		assertThrows(EventNotFoundException.class, () -> pairingService.generatePairings(EVENT_ID, ROUND_ID));
		verify(matchRepository, never()).saveAll(any());
	}
	
	@ParameterizedTest
	@EnumSource(value = EventStatus.class, names = {"IN_PROGRESS", "FINISHED"})
	void shouldThrowTournamentNotEditableExceptionWhenEventStatusInProgressOrFinished(EventStatus status) {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).build();
		Event event = Event.builder().id(EVENT_ID).status(status).build();
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		
		// Act and Assert
		assertThrows(TournamentNotEditableException.class, () -> pairingService.generatePairings(EVENT_ID, ROUND_ID));
		verify(matchRepository, never()).saveAll(any());
	}
	
	@Test
	void shouldThrowPairingExceptionWhenNumberOfCoachesIsOdd() {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).pairingType(PairingType.RANDOM).build();
		Event event = Event.builder().id(EVENT_ID).status(EventStatus.PLANNED).build();
		Coach coach1 = Coach.builder().id(1L).build();
		Coach coach2 = Coach.builder().id(2L).build();
		Coach coach3 = Coach.builder().id(3L).build();
		
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(coachRepository.findByEventIdAndStatus(EVENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2, coach3));
		
		// Act and Assert
		PairingException ex = assertThrows(PairingException.class, () -> pairingService.generatePairings(EVENT_ID, ROUND_ID));
		assertTrue(ex.getMessage().contains("Nombre impair"));
		verify(matchRepository, never()).saveAll(any());
	}
	
	@Test
	void shouldThrowPairingExceptionWhenPairingTypeIsSwiss() {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).pairingType(PairingType.SWISS).build();
		Event event = Event.builder().id(EVENT_ID).status(EventStatus.PLANNED).build();
		Coach coach1 = Coach.builder().id(1L).build();
		Coach coach2 = Coach.builder().id(2L).build();
		
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(coachRepository.findByEventIdAndStatus(EVENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2));
		
		// Act and Assert
		PairingException ex = assertThrows(PairingException.class, () -> pairingService.generatePairings(EVENT_ID, ROUND_ID));
		assertTrue(ex.getMessage().contains("Swiss"));
		verify(matchRepository, never()).saveAll(any());
	}
	
	@Test
	void shouldSaveRandomMatchesWhenPairingTypeIsRandom() {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).pairingType(PairingType.RANDOM).build();
		Event event = Event.builder().id(EVENT_ID).status(EventStatus.PLANNED).build();
		Coach coach1 = Coach.builder().id(1L).build();
		Coach coach2 = Coach.builder().id(2L).build();
		Coach coach3 = Coach.builder().id(3L).build();
		Coach coach4 = Coach.builder().id(4L).build();
		
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(coachRepository.findByEventIdAndStatus(EVENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2, coach3, coach4));
		
		// Act
		pairingService.generatePairings(EVENT_ID, ROUND_ID);
		
		// Assert
		verify(matchRepository).saveAll(matchesCaptor.capture());
		List<Match> savedMatches = matchesCaptor.getValue();
		List<Long> pairedIds = savedMatches.stream()
		        .flatMap(m -> Stream.of(m.getCoach1().getId(), m.getCoach2().getId()))
		        .sorted()
		        .toList();

		assertEquals(2, savedMatches.size());
		assertEquals(List.of(1L, 2L, 3L, 4L), pairedIds);
	}
	
	@Test
	void shouldSaveRandomMatchesWhenPairingTypeIsRandomAndThereIsAChallenge() {
		// Arrange
		Round round = Round.builder().id(ROUND_ID).pairingType(PairingType.RANDOM).build();
		Event event = Event.builder().id(EVENT_ID).status(EventStatus.PLANNED).build();
		Coach coach1 = Coach.builder().id(1L).build();
		Coach coach2 = Coach.builder().id(2L).build();
		Coach coach3 = Coach.builder().id(3L).build();
		Coach coach4 = Coach.builder().id(4L).build();
		Match challenge = Match.builder().coach1(coach1).coach2(coach2).build();
		
		when(roundRepository.findByIdAndEventId(ROUND_ID, EVENT_ID)).thenReturn(Optional.of(round));
		when(eventRepository.findById(EVENT_ID)).thenReturn(Optional.of(event));
		when(coachRepository.findByEventIdAndStatus(EVENT_ID, CoachStatus.VALIDATED)).thenReturn(List.of(coach1, coach2, coach3, coach4));
		when(matchRepository.findByRoundId(ROUND_ID)).thenReturn(List.of(challenge));
		
		// Act
		pairingService.generatePairings(EVENT_ID, ROUND_ID);
		
		// Assert
		verify(matchRepository).saveAll(matchesCaptor.capture());
		List<Match> savedMatches = matchesCaptor.getValue();
		List<Long> pairedIds = savedMatches.stream()
		        .flatMap(m -> Stream.of(m.getCoach1().getId(), m.getCoach2().getId()))
		        .sorted()
		        .toList();

		assertEquals(1, savedMatches.size());
		assertEquals(List.of(3L, 4L), pairedIds);
	}
}
