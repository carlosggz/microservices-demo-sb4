package org.example.moviesapi.infrastructure.components;

import lombok.RequiredArgsConstructor;
import org.example.moviesapi.domain.components.MoviesStore;
import org.example.moviesapi.domain.models.MovieDto;
import org.example.moviesapi.domain.models.MovieInfoDto;
import org.example.moviesapi.domain.models.MoviesFilter;
import org.example.moviesapi.infrastructure.mappers.MovieMapper;
import org.example.moviesapi.infrastructure.persistence.MoviesRepository;
import org.example.moviesapi.infrastructure.persistence.MoviesSpecification;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaMoviesStore implements MoviesStore {

    private final MoviesRepository moviesRepository;
    private final MovieMapper movieMapper;

    @Override
    public boolean existsMovie(String id) {
        return moviesRepository.existsById(id);
    }

    @Override
    public List<MovieInfoDto> getMovies(MoviesFilter filter) {
        return moviesRepository
                .findAll(new MoviesSpecification(filter), Pageable.unpaged())
                .stream()
                .map(movieMapper::toInfoDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDto> getMovie(String id) {
        return moviesRepository
                .findById(id)
                .map(movieMapper::toDetailsDto);
    }

    @Override
    public void deleteMovie(String id) {
        moviesRepository.deleteById(id);
    }

    @Override
    public void saveMovie(MovieDto movieDto) {
        moviesRepository
                .save(moviesRepository
                        .findById(movieDto.id())
                        .map(entity -> movieMapper.updateEntityFromDto(movieDto, entity))
                        .orElseGet(() -> movieMapper.toEntity(movieDto))
                );
    }
}
