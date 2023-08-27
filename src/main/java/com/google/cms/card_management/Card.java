package com.google.cms.card_management;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cms.Users.Activeusers.User;
import com.google.cms.utilities.CONSTANTS;
import com.google.cms.utilities.ColorFormatException;
import com.google.cms.utilities.Shared.Audittrails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;

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

    public Card(String name, String color, String description) {
    }
    public void setStatus(String status) {
        String lowercaseStatus = status.toLowerCase();
        if (lowercaseStatus.equals("to do") || lowercaseStatus.equals("in progress") || lowercaseStatus.equals("done")) {
            this.status = lowercaseStatus;
        } else {
            throw new IllegalArgumentException("Invalid status value. Accepted values are: 'To Do', 'In Progress', 'Done'");
        }
    }
    public void setColor(String color) {
        if (color == null) {
            this.color = null;
        } else if (color.matches("^#[A-Za-z0-9]{6}$")) {
            this.color = color;
        } else {
            throw new ColorFormatException("Color should conform to the format '#RRGGBB'");
        }
    }
    @ManyToOne
    @JsonIgnore
    private User user;
}
