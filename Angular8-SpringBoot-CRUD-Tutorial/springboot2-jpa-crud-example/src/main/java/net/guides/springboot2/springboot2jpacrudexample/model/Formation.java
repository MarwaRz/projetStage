package net.guides.springboot2.springboot2jpacrudexample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@Table(name = "Formation")
@JsonInclude(value=Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Formation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotEmpty
    private String nom;


    @NotEmpty
    @Min(0)

    private String description;
    private String date;




    private String fileName;

    public  Formation(long id, String description, String nom,String date, String fileName) {
        super();
        this.id = id;
        this.nom = nom;
        this.description = description;
     this.date=date ;

        this.fileName = fileName;
    }
    public  Formation() {
        super();

    }


}
