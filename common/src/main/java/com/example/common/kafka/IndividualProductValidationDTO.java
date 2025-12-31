package com.example.common.kafka;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IndividualProductValidationDTO {
    private String id;
    private int delta;
}
