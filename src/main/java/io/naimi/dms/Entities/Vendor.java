package io.naimi.dms.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@SequenceGenerator(name="seq", initialValue=1001, allocationSize=100)
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private Long id;
    @NotEmpty(message = "remplissez ce champ vide.")
    private String cin;
    private String username;
    @NotEmpty (message = "remplissez ce champ vide")
    private String fullName;
    @NotNull (message = "remplissez ce champ vide.")
    private String password;
    @NotNull (message = "remplissez ce champ vide.")
    private String rib;
    @NotNull (message = "remplissez ce champ vide.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    @Email (message = "E-mail invalide.")
    @NotEmpty (message = "remplissez ce champ vide.")
    private String email;
    @NotEmpty (message = "remplissez ce champ vide.")
    @Size(max = 10,message = "Numéro de téléphone invalide.")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateJoined;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "vendor")
    private List<Package> packages;
}