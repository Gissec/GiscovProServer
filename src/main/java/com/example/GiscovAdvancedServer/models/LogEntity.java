package com.example.GiscovAdvancedServer.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "logs_entity", schema = "news")
@Getter
@Setter
public class LogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uri;

    private String authorId;

    private String method;

    private String methodType;

    private String errorMessage;

    private Integer responseCode;

    private LocalDateTime timestamp;
}
