package com.example.system.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AAErequest {
    private Integer uid;

    private Integer s1;

    private Integer s2;

    private Integer s3;

    private Integer s4;

    private Integer s5;
}
