package com.example.GiscovAdvancedServer.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",schema = "news")
public class UserEntity {

        @Id
        @UuidGenerator
        private UUID id;

        private String avatar;

        private String email;

        private String name;

        private String role;

        private String password;

        @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
        private List<NewsEntity> news;
}
