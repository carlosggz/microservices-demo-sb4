package org.example.actorsapi.domain.models;

import lombok.With;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@With
public record ActorDto(
    String id,
    String firstName,
    String lastName,
    String character,
    int likes,
    Set<String> movies){

    public ActorDto {
        Assert.isTrue(StringUtils.isNotBlank(id), "id must not be blank");
        Assert.isTrue(StringUtils.isNotBlank(firstName), "firstName must not be blank");
        Assert.isTrue(StringUtils.isNotBlank(lastName), "lastName must not be blank");
        Assert.isTrue(StringUtils.isNotBlank(character), "character must not be blank");
        Assert.isTrue(likes >= 0, "Likes cannot be negative");
        Assert.isTrue(!CollectionUtils.isEmpty(movies), "movies must not be empty");
    }
}
