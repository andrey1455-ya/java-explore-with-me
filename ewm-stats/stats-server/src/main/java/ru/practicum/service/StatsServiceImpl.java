package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.InvalidDateRangeException;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.EndpointHitDto;
import ru.practicum.model.ViewStatsDto;
import ru.practicum.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

	private final HitRepository hitRepository;

	@Override
	public EndpointHitDto saveHit(EndpointHitDto endpointHitDto) {
		EndpointHit endpointHit = EndpointHitMapper.mapToEndpointHit(endpointHitDto);
		hitRepository.save(endpointHit);
		return EndpointHitMapper.mapToEndpointHitDto(endpointHit);

	}

	@Override
	public List<ViewStatsDto> findHitsByParams(LocalDateTime start,
											   LocalDateTime end,
											   List<String> uris,
											   boolean unique) {
		if (start.isAfter(end)) {
			throw new InvalidDateRangeException("Ошибка даты.");
		}
		if (unique) {
			return hitRepository.findAllUnique(start, end, uris);
		} else {
			return hitRepository.findAll(start, end, uris);
		}
	}
}
