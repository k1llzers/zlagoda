package org.naukma.zlagoda.check.dto;

import lombok.*;
import org.naukma.zlagoda.abstraction.repository.GettableById;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateCheckDto implements GettableById<Integer> {
    private Integer id;
    private Integer customerCardId;
    Map<Integer, Integer> productIdToCountMap;
}
