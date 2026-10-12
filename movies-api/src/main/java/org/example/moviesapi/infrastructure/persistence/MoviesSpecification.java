package org.example.moviesapi.infrastructure.persistence;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.example.moviesapi.domain.models.MoviesFilter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class MoviesSpecification implements Specification<MovieEntity> {
    private final MoviesFilter filter;

    @Override
    public @Nullable Predicate toPredicate(
            @NonNull Root<MovieEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {

        if (filter == null) {
            return criteriaBuilder.conjunction();
        }

        val predicates = new ArrayList<Predicate>();
        addTextFilterIfNotNull(root, criteriaBuilder, predicates);
        addGreaterThanIfNotNull(filter.startYear(), Field.RELEASE_YEAR, root, criteriaBuilder, predicates);
        addLessThanIfNotNull(filter.endYear(), Field.RELEASE_YEAR, root, criteriaBuilder, predicates);
        addGreaterThanIfNotNull(filter.startLikes(), Field.LIKES, root, criteriaBuilder, predicates);
        addLessThanIfNotNull(filter.endLikes(), Field.LIKES, root, criteriaBuilder, predicates);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void addTextFilterIfNotNull(Root<MovieEntity> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional
                .ofNullable(filter.text())
                .filter(StringUtils::isNotBlank)
                .ifPresent(text -> {
                    val pattern = "%" + text.toLowerCase() + "%";
                    val fieldPredicates = Stream
                            .of(Field.ID, Field.TITLE, Field.IMDB)
                            .map(field -> cb.like(cb.lower(root.get(field.getFieldName())), pattern))
                            .toList();
                    predicates.add(cb.or(fieldPredicates));
                });
    }

    private void addGreaterThanIfNotNull(Integer value, Field field, Root<MovieEntity> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional
                .ofNullable(value)
                .ifPresent(v -> predicates.add(cb.greaterThanOrEqualTo(root.get(field.getFieldName()), v)));
    }

    private void addLessThanIfNotNull(Integer value, Field field, Root<MovieEntity> root, CriteriaBuilder cb, List<Predicate> predicates) {
        Optional
                .ofNullable(value)
                .ifPresent(v -> predicates.add(cb.lessThanOrEqualTo(root.get(field.getFieldName()), v)));
    }


    @RequiredArgsConstructor
    private enum Field {
        RELEASE_YEAR("releaseYear"),
        LIKES("likes"),
        ID("id"),
        TITLE("title"),
        IMDB("imdb");

        @Getter
        private final String fieldName;
    }
}
