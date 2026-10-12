package org.example.moviesapi.domain.models;

import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.time.LocalDate;

@With
public record MovieDto(
        String id,
        String title,
        int releaseYear,
        String imdb,
        int likes
){
    public MovieDto {
        Assert.isTrue(StringUtils.isNotBlank(id), "Id cannot be null");
        Assert.isTrue(StringUtils.isNotBlank(title), "Title cannot be null");
        Assert.isTrue(releaseYear >= 1960 && releaseYear <= LocalDate.now().getYear(),
                "Year must be between 1960 and current year");
        Assert.isTrue(StringUtils.isNotBlank(imdb), "Imdb cannot be null");
        Assert.isTrue(likes >= 0, "Likes cannot be negative");
    }
}
