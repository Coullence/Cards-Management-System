package com.google.cms.role;

import com.google.cms.utilities.Shared.Audittrails;
import lombok.*;

import javax.persistence.*;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
@Table(name = "roles")
public class Role extends Audittrails {
    @Column(length = 250, nullable = false, unique = true)
    private String name;
    private String preAuthName;
    private String status;
}