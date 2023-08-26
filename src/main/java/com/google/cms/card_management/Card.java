package com.google.cms.card_management;

import com.google.cms.utilities.CONSTANTS;
import com.google.cms.utilities.Shared.Audittrails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class Card extends Audittrails {
    @Column(nullable = false)
    private String name;
    private String color;
    private String description;
    @Column(nullable = false)
    private String status = CONSTANTS.TODO;

    public Card(String name, String color) {
        this.name = name;
        this.color = color;
        setStatus(CONSTANTS.TODO);
    }
    public void setColor(String color) {
        if (color == null || color.isEmpty()) {
            this.color = null;
        } else if (color.matches("^#[A-Za-z0-9]{6}$")) {
            this.color = color;
        } else {
            throw new IllegalArgumentException("Color should conform to the format '#RRGGBB'");
        }
    }
}
