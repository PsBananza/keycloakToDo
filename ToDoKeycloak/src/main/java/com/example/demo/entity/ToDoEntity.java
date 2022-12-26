package com.example.demo.entity;

import java.util.Date;

import com.example.demo.exception.LengthLimit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Entity
@Table(name = "todo")
@Data
public class ToDoEntity {
    @CreationTimestamp
    private Date createdAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean status = false;
    private String text;
    @UpdateTimestamp
    private Date updatedAt;





    public String getText() {
        return text;
    }

    public void setText(String text)throws LengthLimit {
        if (text.length() < 3 || text.length() > 160) {
            throw new LengthLimit("Введите больше 3 симвалов и меньше 160");
        }
        this.text = text;
    }
}
