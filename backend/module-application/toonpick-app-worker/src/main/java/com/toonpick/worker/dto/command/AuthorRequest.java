package com.toonpick.worker.dto.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthorRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    @NotNull(message = "author의 name은 null일 수 없습니다.")
    private String name;

    @JsonProperty("role")
    @NotNull(message = "author의 role은 null일 수 없습니다.")
    private String role;
}